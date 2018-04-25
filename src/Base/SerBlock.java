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

import Graphics.Point2D;

/**
 * The serializable Block class used for saving/loading scene.
 */
public class SerBlock implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6502709922624215662L;

	/** The block. */
	Block block;

	/** The position. */
	Point2D position;

	/** The size. */
	Point2D size;

	/**
	 * Instantiates a new serializable block.
	 *
	 * @param block
	 *            the block
	 * @param position
	 *            the position
	 * @param size
	 *            the size
	 */
	public SerBlock(Block block, Point2D position, Point2D size) {
		super();
		this.block = block;
		this.position = position;
		this.size = size;
	}

	/**
	 * Gets the block.
	 *
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Point2D getPosition() {
		return position;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Point2D getSize() {
		return size;
	}

}
