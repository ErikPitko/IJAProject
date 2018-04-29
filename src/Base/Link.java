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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Graphics.DrawableObject;
import Graphics.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * The Link class.
 */
public class Link implements DrawableObject, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7233283247452423376L;

	/** Constant specifying distance between line and center of the port */
	private static final double PORT_LINK_OFFSET = Port.PORT_SIZE / 2 + 1;
	
	/** The input port. */
	private Port inPort;

	/** The output port. */
	private Port outPort;

	/** The graphical line representation */
	private transient Line line;

	/** The loop flag */
	private boolean isCycled;

	/** List of looped links */
	private static List<Line> cycledLinks = new ArrayList<>();

	/** The background of value displayed on mouse interaction */
	public Rectangle valuePopupBg;

	/** The text in popup displayed on mouse interaction */
	public Text valuePopupTxt;

	/**
	 * Instantiates a new link.
	 */
	public Link() {
		super();
		line = new Line();
		isCycled = false;
	}

	/**
	 * Instantiates a new link from existing link.
	 *
	 * @param link
	 *            the old link
	 */
	public Link(Link link) {
		super();
		line = link.line;
		setOutPort(link.outPort);
		setInPort(link.inPort);
		isCycled = link.isCycled;
	}

	/**
	 * Instantiates a new link and links given ports.
	 *
	 * @param inPort
	 *            the input port
	 * @param outPort
	 *            the output port
	 */
	public Link(Port inPort, Port outPort) {
		super();
		setInPort(inPort);
		line = new Line();
		setOutPort(outPort);
		isCycled = false;
	}

	/**
	 * Gets the input port.
	 *
	 * @return the input port
	 */
	public Port getInPort() {
		return inPort;
	}

	/**
	 * Sets the input port.
	 *
	 * @param inPort
	 *            the new input port
	 */
	public void setInPort(Port inPort) {
		if (this.outPort != null && inPort != null)
			Block.unsetCalculated(outPort.GetBlock());
		this.inPort = inPort;
		if (inPort != null)
			inPort.setLink(this);
	}

	/**
	 * Gets the output port.
	 *
	 * @return the output port
	 */
	public Port getOutPort() {
		return outPort;
	}

	/**
	 * Sets the output port.
	 *
	 * @param outPort
	 *            the new output port
	 */
	public void setOutPort(Port outPort) {
		if (this.outPort != null && outPort != null)
			Block.unsetCalculated(outPort.GetBlock());
		this.outPort = outPort;
		if (outPort != null)
			outPort.setLink(this);
	}

	/**
	 * Gets the line.
	 *
	 * @return the line
	 */
	public Line getLine() {
		return line;
	}

	/**
	 * Removes the line.
	 */
	public void Remove() {
		outPort = null;
		inPort = null;
	}

	/**
	 * Checks if the link is cycled.
	 *
	 * @return true, if successful
	 */
	public boolean IsCycled() {
		return isCycled;
	}

	/**
	 * Sets the link loop flag.
	 */
	public void SetCycled() {
		isCycled = true;
		cycledLinks.add(line);
	}

	/**
	 * Set loop flag to false.
	 */
	public void UnSetCycled() {
		isCycled = false;
		cycledLinks.remove(line);
	}
	
	/**
	 * Sets start and end point of the line on the circumference of the port
	 */
	private void calculateLinkPortConnection() {
		Point2D point1 = new Point2D(inPort.Rect.Center().X, inPort.Rect.Center().Y);
		Point2D point2 = new Point2D(outPort.Rect.Center().X, outPort.Rect.Center().Y);
		double angle = Point2D.GetAngleBetweenTwoPoints(point1, point2);

		line.setStartX(point1.X + (Math.cos(angle) * PORT_LINK_OFFSET));
		line.setStartY(point1.Y + (Math.sin(angle) * PORT_LINK_OFFSET));
		line.setEndX(point2.X + (Math.cos(angle+ Math.PI) * PORT_LINK_OFFSET));
		line.setEndY(point2.Y + (Math.sin(angle+ Math.PI) * PORT_LINK_OFFSET));
	}

	/**
	 * @see Graphics.DrawableObject#Draw(javafx.scene.layout.AnchorPane)
	 */
	@Override
	public void Draw(AnchorPane pane) {
		if (inPort == null || outPort == null)
			return;
		if (!pane.getChildren().contains(line)) {
			calculateLinkPortConnection();
			if (isCycled)
				line.setStroke(Color.RED);
			else
				line.setStroke(Color.BLACK);
			line.setStrokeWidth(3);
			line.setOnMouseEntered(event -> {
				valuePopupBg = new Rectangle();
				valuePopupBg.setX(event.getX() + 1);
				valuePopupBg.setY(event.getY() + 1);
				valuePopupTxt = new Text();
				valuePopupTxt.setText(String.valueOf(inPort.GetBlock().getValue()));
				valuePopupTxt.setFill(Color.BLACK);
				valuePopupTxt.setX(event.getX() + valuePopupTxt.getBoundsInLocal().getWidth() / 2);
				valuePopupTxt.setY(event.getY() - valuePopupTxt.getBoundsInLocal().getHeight() / 3);
				valuePopupBg.setX(event.getX() + 1);
				valuePopupBg.setY(event.getY() - valuePopupTxt.getBoundsInLocal().getHeight());
				valuePopupBg.setHeight(valuePopupTxt.getBoundsInLocal().getHeight());
				valuePopupBg.setWidth(valuePopupTxt.getBoundsInLocal().getWidth() + 20);
				valuePopupBg.setFill(Color.WHITE);
				valuePopupBg.setStroke(Color.BLACK);
				pane.getChildren().add(valuePopupBg);
				pane.getChildren().add(valuePopupTxt);

			});
			line.setOnMouseExited(event -> {
				pane.getChildren().remove(valuePopupBg);
				pane.getChildren().remove(valuePopupTxt);
			});
			pane.getChildren().add(line);
		}
	}

}