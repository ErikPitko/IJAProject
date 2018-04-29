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


/**
 * The Point2D class.
 */
public class Point2D implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3647197621858265823L;
	
	/** The x coord. */
	public double X;
	
	/** The y coord. */
	public double Y;

	/**
	 * Instantiates a new point2D.
	 *
	 * @param x the x coord
	 * @param y the y coord
	 */
	public Point2D(double x, double y) {
		X = x;
		Y = y;
	}

	/**
	 * Instantiates a new point2D on zero position.
	 */
	public Point2D() {
		X = 0;
		Y = 0;
	}

	/**
	 * Calculates distance between two points.
	 *
	 * @param first the first point
	 * @param second the second point
	 * @return the distance
	 */
	public static double Distance(Point2D first, Point2D second) {
		return Math.sqrt(Math.pow(first.X - second.X, 2) + Math.pow(first.Y - second.Y, 2));
	}

	/**
	 * Calculates vector from points.
	 *
	 * @param first the first point
	 * @param second the second point
	 * @return the point2D vector
	 */
	public static Point2D Vector(Point2D first, Point2D second) {
		return new Point2D(second.X - first.X, second.Y - first.Y);
	}
	
	/**
	 * Determines the angle of a straight line drawn between point one and two.
	 * @param p1
	 * @param p2
	 * @return how much we have to rotate a horizontal line counter-clockwise for it to match the line between the two points
	 */
	public static double GetAngleBetweenTwoPoints(Point2D p1, Point2D p2)
    {
        double xDiff = p2.X - p1.X;
        double yDiff = p2.Y - p1.Y;
        return (Math.atan2(yDiff, xDiff));
    }

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Point: X = " + X + " Y = " + Y;
	}

	/**
	 * Subtracts given point.
	 *
	 * @param other point to be subtracted
	 * @return newly created point2D point
	 */
	public Point2D sub(Point2D other) {
		return new Point2D(this.X - other.X, this.Y - other.Y);
	}

	/**
	 * Adds given point.
	 *
	 * @param other point to be added
	 * @return newly created point2D
	 */
	public Point2D add(Point2D other) {
		return new Point2D(this.X + other.X, this.Y + other.Y);
	}
}
