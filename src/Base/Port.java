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
import Graphics.MainWindowController;
import Graphics.Rect;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * The Port class.
 */
public class Port implements DrawableObject, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2425142487765901647L;
	
	/** @see Block#_rect */
	public Rect Rect;
	
	/** The block in which port resides */
	private Block _block;
	
	/** The list of links connected to the port */
	private List<Link> _link;
	
	/** The background color. */
	private transient Color _backgroundColor;
	
	/** The Constant PORT_SIZE. */
	public static final int PORT_SIZE = 15;

	/**
	 * Gets the list of links.
	 *
	 * @return the list of links
	 */
	public List<Link> GetLinks() {
		return _link;
	}

	/**
	 * Gets the first link in list.
	 *
	 * @return the link
	 */
	public Link GetFirstLink() {
		if (_link.size() > 0)
			return _link.get(0);
		return null;
	}

	/**
	 * Adds link to port if it does not exist already.
	 *
	 * @param link the new link
	 */
	public void setLink(Link link) {
		if (!_link.contains(link))
			this._link.add(link);
	}

	/**
	 * Deletes all links connected to the port.
	 */
	public void unSetLink() {
		for (int i = 0; i < _link.size();) {
			Link middle = _link.get(i);
			if (middle.getOutPort() != null)
				Block.UnsetCalculated(middle.getOutPort().GetBlock());
			Port in = middle.getOutPort();
			if (in != null) {
				in.GetLinks().remove(middle);
			}
			this._link.remove(middle);
			if (MainWindowController.AnchorPanel != null)
			{
				MainWindowController.AnchorPanel.getChildren().remove(middle.valuePopupBg);
				MainWindowController.AnchorPanel.getChildren().remove(middle.valuePopupTxt);
				MainWindowController.AnchorPanel.getChildren().remove(middle.getLine());
			}
			middle.Remove();
		}
	}

	/**
	 * Gets the block.
	 *
	 * @return the block
	 */
	public Block GetBlock() {
		return _block;
	}

	/**
	 * Instantiates a new port.
	 *
	 * @param rect graphic properties of the port
	 * @param block the block in which given port resides
	 */
	public Port(Rect rect, Block block) {
		_link = new ArrayList<>();
		Rect = rect;
		_block = block;
		_backgroundColor = Color.WHITE;
	}

	/**
	 * Instantiates a new port.
	 *
	 * @param rect graphic properties of the port
	 * @param block the block in which given port resides
	 * @param color the color of the port
	 */
	public Port(Rect rect, Block block, Color color) {
		_link = new ArrayList<>();
		Rect = rect;
		_block = block;
		_backgroundColor = color;
	}

	/**
	 * Sets the default color of port.
	 */
	public void SetDefaultColor()
	{
		Rect.setFill(_backgroundColor);
	}

	/**
	 * @see Graphics.DrawableObject#Draw(javafx.scene.layout.AnchorPane)
	 */
	@Override
	public void Draw(AnchorPane pane) {
		Rect.setFill(_backgroundColor);
		Rect.setStroke(Color.BLACK);
		pane.getChildren().add(Rect);
	}
}
