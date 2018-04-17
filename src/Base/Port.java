package Base;

import Graphics.DrawableObject;
import Graphics.Rect;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Port implements DrawableObject {

    public Rect Rect;
    private Block _block;
    private Link _link;
    private Color _backgroundColor;
    public static final int PORT_SIZE = 10;

    public Link GetLink() {
        return _link;
    }

    public void setLink(Link link) {
        this._link = link;
    }

    public Block GetBlock()
    {
        return _block;
    }

    public Port(Rect rect, Block block)
    {
        Rect = rect;
        _block = block;
        _backgroundColor = Color.WHITE;
    }

    public Port(Rect rect, Block block,Color color)
    {
        Rect = rect;
        _block = block;
        _backgroundColor = color;
    }


    @Override
    public void Draw(AnchorPane pane)
    {
        Rectangle r = new Rectangle();
        r.setX(Rect.X);
        r.setY(Rect.Y);
        r.setWidth(Rect.Width());
        r.setHeight(Rect.Height());
        r.setFill(_backgroundColor);
        r.setStroke(Color.BLACK);
        pane.getChildren().add(r);
    }

	public Link getLink() {
		return _link;
	}
}
