/*******************************************************************************
 *
 * VUT FIT Brno - IJA project BlockDiagram
 *
 * Copyright (C) 2018, Adam Petras (xpetra19)
 * Copyright (C) 2018, Erik Pitko (xpitko00)
 * 
 * Contributors: 
 * 		Adam Petras - GUI, base application implementation, tests
 * 		Erik Pitko - base application implementation, Doxygen doc, tests, save/load scene
 * 
 ******************************************************************************/
package Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Graphics.DrawableObject;
import Graphics.MainWindowController;
import Graphics.Panel;
import Graphics.Point2D;
import Graphics.Rect;
import javafx.scene.CacheHint;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * The Block class.
 */
public class Block implements DrawableObject, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1383706658730772727L;
	/***
	 * Enum block type.
	 */
	private EBlock _eBlock;
	/***
	 * Graphic properties (position, size).
	 */
	private transient Rect _rect;
	/***
	 * Graphic properties of resizing box.
	 */
	private transient Rect _resizeRect;
	/***
	 * Background image.
	 */
	private transient ImageView image;
	/***
	 * List of input ports.
	 */
	private ArrayList<Port> inPorts = new ArrayList<Port>();

	/** The out port. */
	private Port _outPort;

	/** * Stored value inside block. */
	private double value = 0;
	/***
	 * Variable used for optimization.
	 * 
	 * Individual block is not calculated multiple times in one calculation cycle.
	 */
	private transient boolean calculated = false;

	/** The Constant MINBLOCKSIZE. */
	public static final int MINBLOCKSIZE = 100;

	/** The Constant MAXBLOCKSIZE. */
	public static final int MAXBLOCKSIZE = 400;
	/***
	 * Counter used in debug mode.
	 * 
	 * Each individual block calculation rises counter by 1. Calculation proceeds
	 * until it matches {@link Graphics.Panel#stepCounter}
	 */
	public static int stepCounter = 0;
	/***
	 * Block value shown in debug mode.
	 */
	private transient Text debugDisp;
	/***
	 * Block value inside display block.
	 */
	private transient Text disp;

	/**
	 *
	 * @param eBlock
	 *            type of the block
	 * @param rect
	 *            graphic properties of the block
	 */
	public Block(EBlock eBlock, Rect rect) {
		super();
		this._eBlock = eBlock;
		this._rect = rect;
		if (eBlock != EBlock.OUT)
			_outPort = new Port(
					new Rect(_rect.XMax() - (Port.PORT_SIZE + Port.PORT_SIZE / 2),
							_rect.getY() + _rect.getHeight() / 2 - Port.PORT_SIZE / 2, Port.PORT_SIZE, Port.PORT_SIZE),
					this, Color.RED);
	}

	/**
	 *
	 * @param eBlock
	 *            type of the block
	 * @param rect
	 *            graphic properties of the block
	 * @param value
	 *            internal value of block used by input block
	 */
	public Block(EBlock eBlock, Rect rect, double value) {
		super();
		this._eBlock = eBlock;
		this._rect = rect;
		this.value = value;
		if (eBlock != EBlock.OUT)
			_outPort = new Port(
					new Rect(_rect.XMax() - (Port.PORT_SIZE + Port.PORT_SIZE / 2),
							_rect.getY() + _rect.getHeight() / 2 - Port.PORT_SIZE / 2, Port.PORT_SIZE, Port.PORT_SIZE),
					this, Color.RED);
	}

	/***
	 * Centers input ports in the block.
	 */
	private void CalculatePortsToMiddle() {
		RecalculateHeights();
		double step = _rect.getHeight() / inPorts.size();
		double div = step / 2;
		for (int i = 0; i < inPorts.size(); i++) {
			inPorts.get(i).Rect.setY((int) (_rect.getY() + (i + 1) * step - div - Port.PORT_SIZE / 2));
		}
	}

	/***
	 * Calculates block size according to number of input ports and centers output
	 * block.
	 * 
	 * @return false if block is too small to contain all input ports
	 */
	private boolean RecalculateHeights() {
		if (inPorts.size() * Port.PORT_SIZE >= _rect.getHeight() - 10) {
			_rect.setY(inPorts.size() * Port.PORT_SIZE + inPorts.size() * 15);
			_outPort.Rect.setY(_rect.getY() + _rect.getHeight() / 2 - Port.PORT_SIZE / 2);
			return true;
		}
		return false;
	}

	/***
	 * Generates one input port.
	 * 
	 * calls {@link #CalculatePortsToMiddle()}
	 */
	public void genInPort() {
		Rect newport = new Rect(_rect.getX() + Port.PORT_SIZE / 2,
				_rect.getY() + Port.PORT_SIZE / 2 + Port.PORT_SIZE + 5 * inPorts.size(), Port.PORT_SIZE,
				Port.PORT_SIZE);
		this.inPorts.add(new Port(newport, this));
		CalculatePortsToMiddle();
	}

	/***
	 * Assigns value and changes {@link #debugDisp} and {@link #disp}.
	 * 
	 * @param value
	 *            value to be assigned
	 */
	private void setValue(double value) {
		this.value = value;
		if (debugDisp != null) {
			debugDisp.setText(String.valueOf(value));
			debugDisp.setX(_rect.getX() + _rect.getWidth() - debugDisp.getBoundsInLocal().getWidth());
		}
		if (disp != null) {
			disp.setText(String.valueOf(value));
			disp.setX(_rect.Center().X - disp.getBoundsInLocal().getWidth() / 2);
		}
	}

	/**
	 * Gets the image view.
	 *
	 * @return the image view
	 */
	public ImageView getImageView() {
		return image;
	}

	/**
	 * Recursive searching for loops.
	 *
	 * @param comparing
	 *            Always left null
	 * @param block
	 *            Block to be checked for loop in tree
	 * @return true if loop is found
	 */
	public static boolean isCycled(Block comparing, Block block) {
		boolean found = false;

		if (comparing == null) {
			comparing = block;
		} else {
			if (comparing == block)
				return true;
		}
		if (block == null)
			return found;

		for (Port port : block.getInPorts()) {
			Link frontLink = port.GetFirstLink();
			if (frontLink != null) {
				found = isCycled(comparing, frontLink.getInPort().GetBlock());
				if (found)
					break;
			}
		}
		return found;
	}

	/***
	 * Recursive block calculation from root.
	 * 
	 * Recursively calls all blocks on all input ports and calculates its values.
	 * 
	 * @param block
	 *            root block to be calculated
	 * @return value of given root block
	 */
	public static double compute(Block block) {
		if (isCycled(null, block)) {
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
				if (Block.stepCounter == Panel.stepCounter) {
					return value;
				}
				else
				{
					if(MainWindowController.IsDebug) {
						ImageView image = block.getImageView();
						ColorAdjust blackout = new ColorAdjust();
						blackout.setBrightness(-0.5);
						image.setEffect(blackout);
						image.setCache(true);
						image.setCacheHint(CacheHint.SPEED);
					}
				}
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
		if (block.getType() != EBlock.IN)
			Block.stepCounter++;
		block.calculated = true;
		return block.value;
	}

	/***
	 * Set blocks to be recalculated after a change in links or blocks.
	 * 
	 * Recursively calls each block from given port and sets {@link #calculated} to
	 * false.
	 * 
	 * @param block
	 *            changed block
	 */
	public static void unsetCalculated(Block block) {
		if (block == null)
			return;
		if (block.getType() == EBlock.IN)
			return;
		Panel.stepCounter = 0;
		Block.stepCounter = 0;
		block.calculated = false;
		block.setValue(0);
		if (block._outPort == null)
			return;
		for (int i = 0; i < block._outPort.GetLinks().size(); i++) {
			Link link = block._outPort.GetLinks().get(i);
			if (link == null || link.getOutPort() == null)
				return;
			unsetCalculated(link.getOutPort().GetBlock());
		}
	}

	/**
	 * Gets the input ports.
	 *
	 * @return the input ports
	 */
	public ArrayList<Port> getInPorts() {
		return inPorts;
	}

	/**
	 * Gets the output port.
	 *
	 * @return the output port
	 */
	public Port GetOutPort() {
		return _outPort;
	}

	/**
	 * Sets the output port.
	 *
	 * @param p
	 *            the output port
	 */
	public void SetOutPort(Port p) {
		_outPort = p;
	}

	/**
	 * Sets new list of input ports.
	 *
	 * @param portList
	 *            new input port list
	 */
	public void SetInPorts(List<Port> portList) {
		inPorts.clear();
		inPorts.addAll(portList);
	}

	/**
	 * Sets new input port and calls {@link #CalculatePortsToMiddle()}.
	 *
	 * @param index
	 *            index of input port to be changed
	 * @param newInPort
	 *            new input port
	 */
	public void SetInPort(int index, Port newInPort) {
		inPorts.set(index, newInPort);
		CalculatePortsToMiddle();
	}

	/**
	 * Gets the the debug display.
	 *
	 * @return the debug display.
	 */
	public Text GetDebugText()
	{
		return debugDisp;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Gets the resizing rectangle.
	 *
	 * @return the rectangle
	 */
	public Rect GetResizeRect() {
		return _resizeRect;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public EBlock getType() {
		return _eBlock;
	}

	/***
	 * Sets new position, does not redraw in GUI.
	 * 
	 * @param position
	 *            new position
	 */
	public void setRectPosition(Point2D position) {
		_rect.setX(position.X);
		_rect.setY(position.Y);
	}

	/**
	 * Gets the rectangle.
	 *
	 * @return the rectangle
	 */
	public Rect getRect() {
		return _rect;
	}

	/**
	 * Changes type of a block and calls {@link #unsetCalculated(Block)}.
	 *
	 * @param eBlock
	 *            new block type
	 */
	public void setType(EBlock eBlock) {
		this._eBlock = eBlock;
		unsetCalculated(this);
	}

	/**
	 * Moves block by value.
	 *
	 * @param deltaX
	 *            pixels on X axis
	 * @param deltaY
	 *            pixels on Y axis
	 */
	public void Move(double deltaX, double deltaY) {
		_rect.setX(_rect.getX() + deltaX);
		_rect.setY(_rect.getY() + deltaY);
		_resizeRect.setX(_resizeRect.getX() + deltaX);
		_resizeRect.setY(_resizeRect.getY() + deltaY);
		if (_outPort != null) {
			_outPort.Rect.setX(_outPort.Rect.getX() + deltaX);
			_outPort.Rect.setY(_outPort.Rect.getY() + deltaY);
		}
		image.setX(image.getX() + deltaX);
		image.setY(image.getY() + deltaY);
		debugDisp.setX(debugDisp.getX() + deltaX);
		debugDisp.setY(debugDisp.getY() + deltaY);
		if (disp != null) {
			disp.setX(disp.getX() + deltaX);
			disp.setY(disp.getY() + deltaY);
		}
		if (_outPort != null)
			for (int i = 0; i < _outPort.GetLinks().size(); i++)
				if (_outPort.GetLinks().get(i) != null) {
					MainWindowController.AnchorPanel.getChildren().remove(_outPort.GetLinks().get(i).getLine());
					_outPort.GetLinks().get(i).Draw(MainWindowController.AnchorPanel);
				}
		for (Port inport : inPorts) {
			inport.Rect.setX(inport.Rect.getX() + deltaX);
			inport.Rect.setY(inport.Rect.getY() + deltaY);
			for (int i = 0; i < inport.GetLinks().size(); i++)
				if (inport.GetLinks().get(i) != null) {
					MainWindowController.AnchorPanel.getChildren().remove(inport.GetLinks().get(i).getLine());
					inport.GetLinks().get(i).Draw(MainWindowController.AnchorPanel);
				}
		}
	}

	/***
	 * Resizes block by value.
	 * 
	 * calls {@link #CalculatePortsToMiddle()}
	 * 
	 * @param deltaX
	 *            pixels on X axis
	 * @param deltaY
	 *            pixels on Y axis
	 */
	public void Resize(double deltaX, double deltaY) {
		if (_rect.getWidth() + deltaX > MINBLOCKSIZE && _rect.getWidth() + deltaX < MAXBLOCKSIZE) {
			_rect.setWidth(_rect.getWidth() + deltaX);
		}
		if (_rect.getHeight() + deltaY >= inPorts.size() * (Port.PORT_SIZE + 5))
			if (_rect.getHeight() + deltaY > MINBLOCKSIZE && _rect.getHeight() + deltaY < MAXBLOCKSIZE) {
				_rect.setHeight(_rect.getHeight() + deltaY);
			}
		_resizeRect.setX(_rect.XMax() - 8);
		_resizeRect.setY(_rect.YMax() - 8);
		debugDisp.setX(_rect.getX() + _rect.getWidth() - debugDisp.getBoundsInLocal().getWidth());
		debugDisp.setY(_rect.getY() - 5);
		if (disp != null) {
			disp.setX(_rect.Center().X - disp.getBoundsInLocal().getWidth() / 2);
			disp.setY(_rect.Center().Y + 5);
		}
		CalculatePortsToMiddle();
		if (_outPort != null) {
			_outPort.Rect.setX(_rect.XMax() - Port.PORT_SIZE - 5);
			_outPort.Rect.setY(_rect.Center().Y - Port.PORT_SIZE / 2);
			for (int i = 0; i < _outPort.GetLinks().size(); i++)
				if (_outPort.GetLinks().get(i) != null) {
					MainWindowController.AnchorPanel.getChildren().remove(_outPort.GetLinks().get(i).getLine());
					_outPort.GetLinks().get(i).Draw(MainWindowController.AnchorPanel);
				}
		}

		for (Port inport : inPorts) {
			for (int i = 0; i < inport.GetLinks().size(); i++)
				if (inport.GetLinks().get(i) != null) {
					MainWindowController.AnchorPanel.getChildren().remove(inport.GetLinks().get(i).getLine());
					inport.GetLinks().get(i).Draw(MainWindowController.AnchorPanel);
				}
		}
		image.setFitWidth(_rect.getWidth());
		image.setFitHeight(_rect.getHeight());
	}

	/***
	 * Deletes Block including all its ports and links.
	 */
	public void DeleteBlock() {
		MainWindowController.AnchorPanel.getChildren().remove(_rect);
		MainWindowController.AnchorPanel.getChildren().remove(image);
		MainWindowController.AnchorPanel.getChildren().remove(debugDisp);
		if (disp != null)
			MainWindowController.AnchorPanel.getChildren().remove(disp);
		if (_outPort != null) {
			for (int i = 0; i < _outPort.GetLinks().size(); i++) {
				unsetCalculated(_outPort.GetBlock());
				_outPort.unSetLink();
			}
			MainWindowController.AnchorPanel.getChildren().remove(_outPort.Rect);
		}
		for (int i = 0; i < inPorts.size(); i++) {
			inPorts.get(i).unSetLink();
			MainWindowController.AnchorPanel.getChildren().remove(inPorts.get(i).Rect);
		}
		MainWindowController.AnchorPanel.getChildren().remove(_resizeRect);
		Panel.BlockList.remove(this);
	}

	/**
	 * Draws block to AnchorPane.
	 * 
	 * @param pane
	 *            the pane
	 * @see Graphics.DrawableObject#Draw(AnchorPane)
	 */
	@Override
	public void Draw(AnchorPane pane) {
		image = new ImageView(new Image(getClass().getResourceAsStream("/Res/" + _eBlock.toString() + ".png")));
		image.setFitHeight(256);
		image.setFitWidth(256);
		
		image.setX(_rect.getX());
		image.setY(_rect.getY());

		
		Rect clip = new Rect(_rect);
		clip.setWidth(256);
		clip.setHeight(256);
		clip.setArcWidth(25);
		clip.setArcHeight(25);
		image.setClip(clip);

		// snapshot the rounded image.
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage wImage = image.snapshot(parameters, null);

		// remove the rounding clip so that our effect can show through.
		image.setClip(null);

		// apply a shadow effect.
		image.setEffect(new DropShadow(20, Color.BLACK));

		// store the rounded image in the imageView.
		image.setImage(wImage);
		image.setFitHeight(_rect.getHeight());
		image.setFitWidth(_rect.getWidth());

		Font font = null;
		try {
			font = Font.loadFont(new FileInputStream(new File("src/Res/fonts/Crasns.ttf")), 15);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		debugDisp = new Text(String.valueOf(value));
		// debugDisp.setFont(font);
		debugDisp.setX(_rect.getX() + _rect.getWidth() - debugDisp.getBoundsInLocal().getWidth());
		debugDisp.setY(_rect.getY() - 5);
		debugDisp.setMouseTransparent(true);
		debugDisp.setVisible(false);
		if (_eBlock == EBlock.OUT) {
			disp = new Text(String.valueOf(value));
			disp.setMouseTransparent(true);
			disp.setFont(font);
			disp.setX(_rect.Center().X - disp.getBoundsInLocal().getWidth() / 2);
			disp.setY(_rect.Center().Y + 5);
			disp.setTextAlignment(TextAlignment.CENTER);
			pane.getChildren().addAll(_rect, image,debugDisp, disp);
		} else
			pane.getChildren().addAll(_rect, image,debugDisp);
		_resizeRect = new Rect(_rect.XMax() - 8, _rect.YMax() - 8, 8, 8);
		_resizeRect.setFill(Color.WHITE);
		_resizeRect.setStroke(Color.BLACK);
		pane.getChildren().add(_resizeRect);
		for (Port p : inPorts) {
			p.Draw(pane);
			for (Link link : p.GetLinks()) {
				if (link != null) {
					link.Draw(pane);
				}
			}
		}
		if (_outPort != null)
			_outPort.Draw(pane);
	}
}
