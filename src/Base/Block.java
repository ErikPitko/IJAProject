package Base;

import java.util.ArrayList;

import Graphics.DrawableObject;
import Graphics.Rect;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block implements DrawableObject
{
    private EBlock _eBlock;
    private Rect _rect;
    private ArrayList<Port> inPorts = new ArrayList<Port>();
	private Port _outPort;
	private double value = 0;
	private boolean calculated = false;

	public Block(EBlock eBlock, Rect rect) {
		super();
		this._eBlock = eBlock;
		this._rect = rect;
		_outPort = new Port(new Rect(_rect.XMax()-(Port.PORT_SIZE+Port.PORT_SIZE/2),_rect.Y+_rect.Height()/2-Port.PORT_SIZE/2,Port.PORT_SIZE,Port.PORT_SIZE),this,Color.RED);
	}

	public Block(EBlock eBlock, Rect rect, double value) {
		super();
		this._eBlock = eBlock;
		this._rect = rect;
		this.value = value;
//		TODO newport
		Rect newport = new Rect(0, 0, 0, 0);
		_outPort = new Port(new Rect(_rect.XMax()-(Port.PORT_SIZE+Port.PORT_SIZE/2),_rect.Y+_rect.Height()/2-Port.PORT_SIZE/2,Port.PORT_SIZE,Port.PORT_SIZE),this,Color.RED);
	}

	private void CalculatePortsToMiddle() {
		boolean resized = RecalculateHeights();
		double step = _rect.Height() / inPorts.size();
		double div = step / 2;
		for (int i = 0; i < inPorts.size(); i++)
		{
			inPorts.get(i).Rect.Y = (int) (_rect.Y + (i + 1) * step - div- Port.PORT_SIZE / 2);
		}
	}

	private boolean RecalculateHeights()
	{
		if(inPorts.size()*Port.PORT_SIZE >= _rect.Height()-10)
		{
			_rect.SetSizeY(inPorts.size()*Port.PORT_SIZE + inPorts.size()*15);
			_outPort.Rect.Y = _rect.Y+_rect.Height()/2-Port.PORT_SIZE/2;
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
//		TODO newport
		Rect newport = new Rect(_rect.X+5,_rect.Y+5 + 15*inPorts.size(),10,10);
		this.inPorts.add(new Port(newport, this));
		CalculatePortsToMiddle();
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
					block.value = value;
					continue;
				}
				switch (block.getType()) {
				case ADD:
					block.value += value;
					break;
				case SUB:
					block.value -= value;
					break;
				case MUL:
					block.value *= value;
					break;
				case DIV:
					block.value /= value;
					break;
				default:
					block.value = value;
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

	public void setType(EBlock eBlock) {
		this._eBlock = eBlock;
		unsetCalculated(this);
	}

    @Override
    public void Draw(AnchorPane pane)
    {
		Rectangle r = new Rectangle();
		r.setX(_rect.X);
		r.setY(_rect.Y);
		r.setWidth(_rect.Width());
		r.setHeight(_rect.Height());
		r.setFill(new Color(0.8,0.8,0.8,1));
		r.setStroke(Color.BLACK);
		pane.getChildren().add(r);
		for (Port p: inPorts)
		{
			p.Draw(pane);
		}
		_outPort.Draw(pane);
    }
}
