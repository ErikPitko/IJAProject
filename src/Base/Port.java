package Base;

import Graphics.DrawableObject;
import Graphics.Rect;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Port implements DrawableObject {

    public Rect Rect;
    private Block _block;
    private List<Link> _link;
    private Color _backgroundColor;
    public static final int PORT_SIZE = 15;

    public List<Link> GetLinks() {
        return _link;
    }

    public Link GetFirstLink() {
        if(_link.size()>0)
            return _link.get(0);
        return null;
    }

    public void setLink(Link link) {
        this._link.add(link);
    }

    public Block GetBlock()
    {
        return _block;
    }

    public Port(Rect rect, Block block)
    {
        _link = new ArrayList<>();
        Rect = rect;
        _block = block;
        _backgroundColor = Color.WHITE;
    }

    public Port(Rect rect, Block block,Color color)
    {
        _link = new ArrayList<>();
        Rect = rect;
        _block = block;
        _backgroundColor = color;
    }

    @Override
    public void Draw(AnchorPane pane)
    {
        Rect.setFill(_backgroundColor);
        Rect.setStroke(Color.BLACK);
        pane.getChildren().add(Rect);
    }
}
