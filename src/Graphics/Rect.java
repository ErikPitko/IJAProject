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
package Graphics;

import java.io.Serializable;

import javafx.scene.shape.Rectangle;



/**
 * The Rectangle class.
 */
public class Rect extends Rectangle implements Serializable {
	private transient static final int ARC_WIDTH = 35;
	private transient static final int ARC_HEIGHT = 35;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9185232623645758681L;

	/**
	 * Gets minimal x axis coord.
	 *
	 * @return leftmost x axis coord
	 */
	public double XMin() {
		return getX();
	}

	/**
	 * Gets maximal x axis coord.
	 *
	 * @return rightmost x axis coord
	 */
	public double XMax() {
		return getX() + getWidth();
	}

	/**
	 * Gets minimal y axis coord.
	 *
	 * @return bottom y axis coord
	 */
	public double YMin() {
		return getY();
	}

	/**
	 * Gets maximal x axis coord.
	 *
	 * @return top y axis coord
	 */
	public double YMax() {
		return getY() + getHeight();
	}

	/**
	 * @return Rectangle position
	 */
	public Point2D Position() {
		return new Point2D(getX(), getY());
	}

	/**
	 * @return Rectangle dimensions
	 */
	public Point2D Size() {
		return new Point2D(getWidth(), getHeight());
	}

	/**
	 * @return Center of the rectangle
	 */
	public Point2D Center() {
		return new Point2D(getX() + (getWidth() / 2), getY() + (getHeight() / 2));
	}

	/**
	 * Instantiates a new rectangle from existing one.
	 *
	 * @param rect copied rectangle
	 */
	public Rect(Rect rect) {
		super();
		setX(rect.getX());
		setY(rect.getY());
		setWidth(rect.getWidth());
		setHeight(rect.getHeight());
		this.setArcWidth(ARC_WIDTH); 
		this.setArcHeight(ARC_HEIGHT); 
	}

	/**
	 * Instantiates a new rectangle.
	 *
	 * @param position position of created rectangle
	 * @param size dimensions of rectangle
	 */
	public Rect(Point2D position, Point2D size) {
		super();
		setX(position.X);
		setY(position.Y);
		setWidth(size.X);
		setHeight(size.Y);
		this.setArcWidth(ARC_WIDTH); 
		this.setArcHeight(ARC_HEIGHT); 
	}

	/**
	 * Instantiates a new rectangle.
	 *
	 * @param positionX the x coord
	 * @param positionY the y coord
	 * @param size dimensions of rectangle
	 */
	public Rect(double positionX, double positionY, Point2D size) {
		super();
		setX(positionX);
		setY(positionY);
		setWidth(size.X);
		setHeight(size.Y);
		this.setArcWidth(ARC_WIDTH); 
		this.setArcHeight(ARC_HEIGHT); 
	}

	/**
	 * Instantiates a new rectangle.
	 *
	 * @param position position of created rectangle
	 * @param sizeX width
	 * @param sizeY height
	 */
	public Rect(Point2D position, double sizeX, double sizeY) {
		super();
		setX(position.X);
		setY(position.Y);
		setWidth(sizeX);
		setHeight(sizeY);
		this.setArcWidth(ARC_WIDTH); 
		this.setArcHeight(ARC_HEIGHT); 
	}

	/**
	 * Instantiates a new rectangle.
	 *
	 * @param positionX the x coord
	 * @param positionY the y coord
	 * @param sizeX width
	 * @param sizeY height
	 */
	public Rect(double positionX, double positionY, double sizeX, double sizeY) {
		super();
		setX(positionX);
		setY(positionY);
		setWidth(sizeX);
		setHeight(sizeY);
		this.setArcWidth(ARC_WIDTH); 
		this.setArcHeight(ARC_HEIGHT); 
	}

	/**
	 * Checks if rectangle contains given point.
	 *
	 * @param point point to be checked
	 * @return true, if successful
	 */
	public boolean Contains(Point2D point) {
		return point.X >= getX() && point.X <= XMax() && point.Y >= getY() && point.Y <= YMax();
	}

	/**
	 * Checks if rectangle intersects with given rectangle.
	 *
	 * @param rect rectangle to be checked
	 * @return true, if successful
	 */
	public boolean Intersect(Rect rect) {
		double xmin = Math.max(getX(), rect.getX());
		double xmax = Math.min(XMax(), rect.XMax());
		if (xmax >= xmin) {
			double ymin = Math.max(getY(), rect.getY());
			double ymax = Math.min(YMax(), rect.YMax());
			if (ymax >= ymin) {
				return true;
			}
		}
		return false;
	}
}
