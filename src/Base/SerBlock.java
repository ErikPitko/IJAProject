package Base;

import java.io.Serializable;

import Graphics.Point2D;

public class SerBlock implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6502709922624215662L;
	Block block;
	Point2D position;
	Point2D size;
	
	public SerBlock(Block block, Point2D position, Point2D size) {
		super();
		this.block = block;
		this.position = position;
		this.size = size;
	}

	public Block getBlock() {
		return block;
	}

	public Point2D getPosition() {
		return position;
	}

	public Point2D getSize() {
		return size;
	}
	
}
