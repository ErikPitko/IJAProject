package Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Graphics.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Block implements DrawableObject, Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1383706658730772727L;
	private EBlock _eBlock;
    private Rect _rect;
    private transient Rect _resizeRect;
    private transient ImageView image;
    private ArrayList<Port> inPorts = new ArrayList<Port>();
	private Port _outPort;
	private double value = 0;
	private transient boolean calculated = false;
	public static final int MINBLOCKSIZE = 100;
	public static final int MAXBLOCKSIZE = 400;
	private transient Text debugDisp;
	private transient Text disp;
	
	public Block(EBlock eBlock, Rect rect) {
		super();
		this._eBlock = eBlock;
		this._rect = rect;
		_outPort = new Port(new Rect(_rect.XMax()-(Port.PORT_SIZE+Port.PORT_SIZE/2),_rect.getY()+_rect.getHeight()/2-Port.PORT_SIZE/2,Port.PORT_SIZE,Port.PORT_SIZE),this,Color.RED);
	}

	public Block(EBlock eBlock, Rect rect, double value) {
		super();
		this._eBlock = eBlock;
		this._rect = rect;
		this.value = value;
		_outPort = new Port(new Rect(_rect.XMax()-(Port.PORT_SIZE+Port.PORT_SIZE/2),_rect.getY()+_rect.getHeight()/2-Port.PORT_SIZE/2,Port.PORT_SIZE,Port.PORT_SIZE),this,Color.RED);
	}

	private void CalculatePortsToMiddle() {
		RecalculateHeights();
		double step = _rect.getHeight() / inPorts.size();
		double div = step / 2;
		for (int i = 0; i < inPorts.size(); i++)
		{
			inPorts.get(i).Rect.setY((int) (_rect.getY() + (i + 1) * step - div- Port.PORT_SIZE / 2));
		}
	}

	private boolean RecalculateHeights()
	{
		if(inPorts.size()*Port.PORT_SIZE >= _rect.getHeight()-10)
		{
			_rect.setY(inPorts.size()*Port.PORT_SIZE + inPorts.size()*15);
			_outPort.Rect.setY(_rect.getY()+_rect.getHeight()/2-Port.PORT_SIZE/2);
			return true;
		}
		return false;
	}
	
//	 public Block(EBlock eBlock, Rect rect, int numOfInput)
//	    {
//	        if(numOfInput < 2)
//	            System.out.println("ERROR");
//	        _eBlock = eBlock;
//	        _rect = rect;
//	        _numOfInput = numOfInput;
//	        _inputPortList = new ArrayList<>();
//	        AddInputPort();
//	    }

//	    public Block(EBlock eBlock, Rect rect, int... inputValues)
//	    {
//	        if(inputValues.length<2)
//	            System.out.println("ERROR");
//	        _eBlock = eBlock;
//	        _rect = rect;
//	        _inputPortList = new ArrayList<>();
//	        _numOfInput = inputValues.length;
//	        AddInputPort();
//	        for (int i = 0; i<_numOfInput;i++)
//	        {
//	            _inputPortList.get(i).SetValue(inputValues[i]);
//	            _inputPortList.get(i).IsValueSet = true;
//	        }
//	    }

//	    public Block(EBlock eBlock,Rect rect)
//	    {
//	        _eBlock = eBlock;
//	        _rect = rect;
//	        _numOfInput = 2;
//	        _inputPortList = new ArrayList<>();
//	        AddInputPort();
//	    }
	
	
	
	
	public void genInPort() {
		Rect newport = new Rect(_rect.getX()+Port.PORT_SIZE/2,_rect.getY()+Port.PORT_SIZE/2 + Port.PORT_SIZE +5*inPorts.size(),Port.PORT_SIZE,Port.PORT_SIZE);
		this.inPorts.add(new Port(newport, this));
		CalculatePortsToMiddle();
	}
	
	private void setValue(double value) {
		this.value = value;
		if(debugDisp != null) {
			debugDisp.setText(String.valueOf(value));
			debugDisp.setX(_rect.getX() + _rect.getWidth() - debugDisp.getBoundsInLocal().getWidth());
		}
		if(disp != null) {
			disp.setText(String.valueOf(value));
			disp.setX(_rect.Center().X - disp.getBoundsInLocal().getWidth()/2);
		}
	}

	public ImageView getImageView()
	{
		return image;
	}
	
	
	/***
	 * @brief Recursive searching for loops
	 * @param comparing Always left null
	 * @param block Block to be checked for loop in tree
	 * @returns true if loop is found
	 */
	public static boolean isCycled(Block comparing, Block block) {
			boolean found = false;
			
			if(comparing == null) {
				comparing = block;
			}else {
				if(comparing == block)
					return true;
			}
			if(block == null)
				return found;
			
			for (Port port : block.getInPorts()) {
				Link frontLink = port.GetFirstLink();
				if (frontLink != null) {
					found = isCycled(comparing, frontLink.getInPort().GetBlock());
					if(found)
						break;
				}
			}
			return found;
	}
	
	public static double compute(Block block) {
		if(isCycled(null, block)) {
			System.err.println("CYCLE !");
			return 0;
		}
		boolean first = true;
		if (block.calculated)
			return block.value;
		for (Port port : block.getInPorts()) {
			Link frontLink = port.GetFirstLink();
			if (frontLink != null) {
				double value = compute(frontLink.getInPort().GetBlock());
				if (first) {
					first = false;
					block.setValue(value);
					continue;
				}
				switch (block.getType()) {
				case ADD:
					block.setValue(block.value + value);
					break;
				case SUB:
					block.setValue(block.value - value);
					break;
				case MUL:
					block.setValue(block.value * value);
					break;
				case DIV:
					block.setValue(block.value / value);
					break;
				default:
					block.setValue(value);
				}
			}
		}
		block.calculated = true;
		return block.value;
	}
	
	public static void unsetCalculated(Block block) {
		if(block == null)
			return;
		block.calculated = false;
		Link link = block._outPort.GetFirstLink();
		if(link== null || link.getOutPort() == null )
			return;
		System.out.println(link.getOutPort());
		unsetCalculated(link.getOutPort().GetBlock());
	}

	public ArrayList<Port> getInPorts() {
		return inPorts;
	}

	public Port GetOutPort(){ return _outPort; }
	public void SetOutPort(Port p)
	{
		_outPort = p;
	}
	public void SetInPorts(List<Port> portList)
	{
		inPorts.clear();
		inPorts.addAll(portList);
	}
	public void SetInPort(int index, Port newInPort)
	{
		inPorts.set(index,newInPort);
		CalculatePortsToMiddle();
	}
	public double getValue() {
		return value;
	}

	public Rect GetResizeRect(){return _resizeRect;}

	public EBlock getType() {
		return _eBlock;
	}
	public void setRectPosition(Point2D position)
	{
		_rect.setX(position.X);
		_rect.setY(position.Y);
	}

	public Rect getRect()
	{
		return _rect;
	}

	public void setType(EBlock eBlock) {
		this._eBlock = eBlock;
		unsetCalculated(this);
	}

	public void Move(double deltaX, double deltaY)
	{
		_rect.setX(_rect.getX() + deltaX);
		_rect.setY(_rect.getY() + deltaY);
		_resizeRect.setX(_resizeRect.getX() + deltaX);
		_resizeRect.setY(_resizeRect.getY() + deltaY);
		_outPort.Rect.setX(_outPort.Rect.getX()+deltaX);
		_outPort.Rect.setY(_outPort.Rect.getY()+deltaY);
		image.setX(image.getX() + deltaX);
		image.setY(image.getY() + deltaY);
		debugDisp.setX(debugDisp.getX()+deltaX);
		debugDisp.setY(debugDisp.getY()+deltaY);
		if(disp != null)
		{
			disp.setX(disp.getX() + deltaX);
			disp.setY(disp.getY() + deltaY);
		}
		for(int i = 0;i<_outPort.GetLinks().size();i++)
			if(_outPort.GetLinks().get(i)!= null)
			{
				FXMLExampleController.AnchorPanel.getChildren().remove(_outPort.GetLinks().get(i).getLine());
				_outPort.GetLinks().get(i).Draw(FXMLExampleController.AnchorPanel);
			}
		for (Port inport: inPorts)
		{
			inport.Rect.setX(inport.Rect.getX()+deltaX);
			inport.Rect.setY(inport.Rect.getY()+deltaY);
			for(int i = 0;i<inport.GetLinks().size();i++)
				if(inport.GetLinks().get(i)!= null)
				{
					FXMLExampleController.AnchorPanel.getChildren().remove(inport.GetLinks().get(i).getLine());
					inport.GetLinks().get(i).Draw(FXMLExampleController.AnchorPanel);
				}
		}
	}

	public void Resize(double deltaX,double deltaY)
	{
		boolean anyIntersects = false;
		if(_rect.getWidth() + deltaX > MINBLOCKSIZE  && _rect.getWidth()+deltaX < MAXBLOCKSIZE)
		{
			for(int i =0 ;i< Panel.BlockList.size();i++)
			{
				if (Panel.BlockList.get(i) != this)
					if (new Rect(_rect.getX(),_rect.getY(),_rect.getWidth()+deltaX,_rect.getHeight()).Intersect(Panel.BlockList.get(i).getRect()))
						anyIntersects = true;
				if(anyIntersects == true)
					break;
			}
			if(!anyIntersects)
				_rect.setWidth(_rect.getWidth() + deltaX);
		}
		anyIntersects = false;
		if(_rect.getHeight()+deltaY >= inPorts.size()*(Port.PORT_SIZE+5))
			if(_rect.getHeight() + deltaY > MINBLOCKSIZE && _rect.getHeight()+deltaY < MAXBLOCKSIZE)
			{
				for(int i =0 ;i< Panel.BlockList.size();i++)
				{
					if (Panel.BlockList.get(i) != this)
						if (new Rect(_rect.getX(),_rect.getY(),_rect.getWidth(),_rect.getHeight()+deltaY).Intersect(Panel.BlockList.get(i).getRect()))
							anyIntersects = true;
					if(anyIntersects == true)
						break;
				}
				if(!anyIntersects)
					_rect.setHeight(_rect.getHeight() + deltaY);
			}
		_resizeRect.setX(_rect.XMax()-8);
		_resizeRect.setY(_rect.YMax()-8);
		debugDisp.setX(_rect.getX() + _rect.getWidth() - debugDisp.getBoundsInLocal().getWidth());
		debugDisp.setY(_rect.getY() - 5);
		if(disp != null)
		{
			disp.setX(_rect.Center().X - disp.getBoundsInLocal().getWidth() / 2);
			disp.setY(_rect.Center().Y + 5);
		}
		CalculatePortsToMiddle();
		_outPort.Rect.setX(_rect.XMax()-Port.PORT_SIZE-5);
		_outPort.Rect.setY(_rect.Center().Y-Port.PORT_SIZE/2);
		for (Link outLinks: _outPort.GetLinks()) {
			outLinks.getLine().setStartX(_outPort.Rect.Center().X+Port.PORT_SIZE/2);
			outLinks.getLine().setStartY(_outPort.Rect.Center().Y);
		}

		for (Port inport: inPorts)
		{
			for (Link inLinks: inport.GetLinks())
			{
				inLinks.getLine().setEndX(inport.Rect.Center().X-Port.PORT_SIZE/2);
				inLinks.getLine().setEndY(inport.Rect.Center().Y);
			}
		}
		image.setFitWidth(_rect.getWidth());
		image.setFitHeight(_rect.getHeight());
	}

	public void DeleteBlock()
	{
		FXMLExampleController.AnchorPanel.getChildren().remove(_rect);
		FXMLExampleController.AnchorPanel.getChildren().remove(image);
		FXMLExampleController.AnchorPanel.getChildren().remove(debugDisp);
		if(disp != null)
			FXMLExampleController.AnchorPanel.getChildren().remove(disp);
		for (int i = 0; i < _outPort.GetLinks().size();i++)
		{
			unsetCalculated(_outPort.GetBlock());
			_outPort.unSetLink();
		}
		FXMLExampleController.AnchorPanel.getChildren().remove(_outPort.Rect);
		for (int i = 0; i < inPorts.size();i++)
		{
			inPorts.get(i).unSetLink();
			FXMLExampleController.AnchorPanel.getChildren().remove(inPorts.get(i).Rect);
		}
		FXMLExampleController.AnchorPanel.getChildren().remove(_resizeRect);
		Panel.BlockList.remove(this);
	}

    @Override
    public void Draw(AnchorPane pane)
    {
		
		image = new ImageView(new Image(getClass().getResourceAsStream("/Res/"+_eBlock.toString()+".png")));
		image.setFitHeight(_rect.getHeight());
		image.setFitWidth(_rect.getWidth());
		image.setX(_rect.getX());
		image.setY(_rect.getY());
		
		Font font = null;
		try {
			font = Font.loadFont(new FileInputStream(new File("src/Res/fonts/Crasns.ttf")), 15);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		debugDisp = new Text(String.valueOf(value));
		debugDisp.setFont(font);
		debugDisp.setX(_rect.getX() + _rect.getWidth() - debugDisp.getBoundsInLocal().getWidth());
		debugDisp.setY(_rect.getY() - 5);
		debugDisp.setMouseTransparent(true);
		
		if (_eBlock == EBlock.OUT) {
			disp = new Text(String.valueOf(value));
			disp.setMouseTransparent(true);
			disp.setFont(font);
			disp.setX(_rect.Center().X - disp.getBoundsInLocal().getWidth()/2);
			disp.setY(_rect.Center().Y + 5);
			disp.setTextAlignment(TextAlignment.CENTER);
			pane.getChildren().addAll(_rect, image, debugDisp, disp);
		}else
			pane.getChildren().addAll(_rect, image, debugDisp);
		_resizeRect = new Rect(_rect.XMax()-8,_rect.YMax()-8,8,8);
		_resizeRect.setFill(Color.WHITE);
		_resizeRect.setStroke(Color.BLACK);
		pane.getChildren().add(_resizeRect);
		for (Port p: inPorts)
		{
			p.Draw(pane);
			for (Link link: p.GetLinks()) {
				if(link!= null)
				{
					link.Draw(pane);
				}
			}
		}
		_outPort.Draw(pane);
    }
}
