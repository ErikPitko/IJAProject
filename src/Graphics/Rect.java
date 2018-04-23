package Graphics;

import java.io.Serializable;

import javafx.scene.shape.Rectangle;

public class Rect extends Rectangle implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -9185232623645758681L;

	public double XMin() {
        return getX();
    }

    public double XMax() { return getX() + getWidth();
    }

    public double YMin() {
        return getY();
    }

    public double YMax() {
        return getY() + getHeight();
    }

    public Point2D Position()
    {
        return new Point2D(getX(),getY());
    }

    public Point2D Size()
    {
        return new Point2D(getWidth(),getHeight());
    }

    public Point2D Center()
    {
        return new Point2D(getX()+(getWidth()/2),getY()+(getHeight()/2));
    }

    public static Rect ZERO = new Rect(0,0,0,0);

    public Rect(Rect rect)
    {
        super();
        setX(rect.getX());
        setY(rect.getY());
        setWidth(rect.getWidth());
        setHeight(rect.getHeight());
    }

    public Rect(Point2D position, Point2D size)
    {
        super();
        setX(position.X);
        setY(position.Y);
        setWidth(size.X);
        setHeight(size.Y);
    }

    public Rect(double positionX,double positionY, Point2D size)
    {
        super();
        setX(positionX);
        setY(positionY);
        setWidth(size.X);
        setHeight(size.Y);
    }

    public Rect(Point2D position, double sizeX,double sizeY)
    {
        super();
        setX(position.X);
        setY(position.Y);
        setWidth(sizeX);
        setHeight(sizeY);
    }

    public Rect(double positionX,double positionY, double sizeX,double sizeY)
    {
        super();
        setX(positionX);
        setY(positionY);
        setWidth(sizeX);
        setHeight(sizeY);
    }

    public boolean Contains(Point2D point)
    {
        return point.X >=  getX() && point.X <= XMax() && point.Y >= getY() && point.Y <= YMax();
    }

    public boolean Intersect(Rect rect)
    {
        double xmin = Math.max(getX(), rect.getX());
        double xmax = Math.min(XMax(), rect.XMax());
        if (xmax >= xmin) {
            double ymin = Math.max(getY(),rect.getY());
            double ymax = Math.min(YMax(), rect.YMax());
            if (ymax >= ymin) {
                return true;
            }
        }
        return false;
    }
}
