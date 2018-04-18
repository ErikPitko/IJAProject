package Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Graphics.DrawableObject;
import Graphics.Point2D;
import Graphics.Rect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Block implements DrawableObject
{
    private EBlock _eBlock;
    private Rect _rect;
    private ArrayList<Port> inPorts = new ArrayList<Port>();
	private Port _outPort;
	private double value = 0;
	private boolean calculated = false;
	
	private Text debugDisp;
	private Text disp;

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
			debugDisp.setTranslateX(_rect.getX() + _rect.getWidth() - debugDisp.getBoundsInLocal().getWidth());
		}
		if(disp != null) {
			disp.setText(String.valueOf(value));
			disp.setTranslateX(_rect.Center().X - disp.getBoundsInLocal().getWidth()/2);
		}
	}
	
	public static double compute(Block block) {
		boolean first = true;
		if (block.calculated)
			return block.value;
		for (Port port : block.getInPorts()) {
			Link frontLink = port.getLink();
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
		Link link = block._outPort.getLink();
		if(link== null)
			return;
		unsetCalculated(link.getOutPort().GetBlock());
	}

	public ArrayList<Port> getInPorts() {
		return inPorts;
	}

	public Port GetOutPort(){ return _outPort; }

	public double getValue() {
		return value;
	}

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

    @Override
    public void Draw(AnchorPane pane)
    {
		
		ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Res/"+_eBlock.toString()+".png")));
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
		debugDisp.setTranslateX(_rect.getX() + _rect.getWidth() - debugDisp.getBoundsInLocal().getWidth());
		debugDisp.setTranslateY(_rect.getY() - 5);
		debugDisp.setMouseTransparent(true);
		
		if (_eBlock == EBlock.OUT) {
			disp = new Text(String.valueOf(value));
			disp.setMouseTransparent(true);
			disp.setFont(font);
			disp.setTranslateX(_rect.Center().X - disp.getBoundsInLocal().getWidth()/2);
			disp.setTranslateY(_rect.Center().Y + 5);
			disp.setTextAlignment(TextAlignment.CENTER);
			pane.getChildren().addAll(_rect, image, debugDisp, disp);
		}else
			pane.getChildren().addAll(_rect, image, debugDisp);
		
		for (Port p: inPorts)
		{
			p.Draw(pane);
			if(p.GetLink()!= null){
				p.GetLink().Draw(pane);
			}
		}
		_outPort.Draw(pane);
    }
}
