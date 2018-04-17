package Base;

import Graphics.DrawableObject;
import Graphics.Rect;
import javafx.scene.canvas.GraphicsContext;

public class Port implements DrawableObject {

    public Rect Rect;
    private Block _block;
    private Link _link;

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
    }


    @Override
    public void Draw(GraphicsContext gc)
    {

    }

	public Link getLink() {
		return _link;
	}
}
