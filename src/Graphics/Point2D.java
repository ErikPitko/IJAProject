package Graphics;

import java.io.Serializable;

public class Point2D implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3647197621858265823L;
	public double X;
    public double Y;

    public Point2D(double x,double y)
    {
        X = x;
        Y = y;
    }

    public Point2D()
    {

    }

    public static double Distance(Point2D first, Point2D second)
    {
        return Math.sqrt(Math.pow(first.X -second.X,2)+Math.pow(first.Y -second.Y,2));
    }

    public static Point2D Vector(Point2D first, Point2D second)
    {
        return new Point2D(second.X-first.X,second.Y-first.Y);
    }

    @Override
    public String toString() {
        return "Point: X = "+ X + " Y = "+ Y;
    }

    public Point2D sub(Point2D other){
        return new Point2D(this.X - other.X, this.Y - other.Y);
    }
    public Point2D add(Point2D other){
        return new Point2D(this.X + other.X, this.Y + other.Y);
    }
}
