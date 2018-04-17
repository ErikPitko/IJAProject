package Base;

import java.util.ArrayList;

import Graphics.DrawableObject;
import Graphics.Rect;
import javafx.scene.canvas.GraphicsContext;



interface MyCallBack
{
    double MathFunction(double a, double b);
}

public class Block implements DrawableObject
{
    private EBlock _eBlock;
    private Rect _rect;
    private ArrayList<Port> inPorts = new ArrayList<Port>();
	private ArrayList<Port> outPorts = new ArrayList<Port>();
	private double value = 0;
	private boolean calculated = false;

	public Block(EBlock eBlock, Rect rect) {
		super();
		this._eBlock = eBlock;
		this._rect = rect;
	}

	public Block(EBlock eBlock, Rect rect, double value) {
		super();
		this._eBlock = eBlock;
		this._rect = rect;
		this.value = value;
//		TODO newport
		Rect newport = new Rect(0, 0, 0, 0);
		outPorts.add(new Port(newport, this));
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
		Rect newport = new Rect(0,0,0,0);
		this.inPorts.add(new Port(newport, this));
	}

	
	public void genOutPort() {
//		TODO newport
		Rect newport = new Rect(0,0,0,0);
		this.outPorts.add(new Port(newport, this));
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
		for (Port port:block.getOutPorts()) {
			Link link = port.getLink();
			if(link == null)
				return;
			unsetCalculated(link.getOutPort().GetBlock());
		}
	}

	public ArrayList<Port> getInPorts() {
		return inPorts;
	}

	public ArrayList<Port> getOutPorts() {
		return outPorts;
	}

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
    public void Draw(GraphicsContext gc)
    {

    }
}
