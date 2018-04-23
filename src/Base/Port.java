package Base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Graphics.DrawableObject;
import Graphics.FXMLExampleController;
import Graphics.Rect;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class Port implements DrawableObject, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2425142487765901647L;
	public Rect Rect;
    private Block _block;
    private List<Link> _link;
    private transient Color _backgroundColor;
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
        if(!_link.contains(link))
            this._link.add(link);
    }

    public void unSetLink()
    {
        for (int i =0;i<_link.size();)
        {
            Link middle = _link.get(i);
            if(middle.getOutPort() != null)
                Block.unsetCalculated(middle.getOutPort().GetBlock());
            Port in = middle.getOutPort();
            if(in != null) {
                in.GetLinks().remove(middle);
            }
            this._link.remove(middle);
            FXMLExampleController.AnchorPanel.getChildren().remove(middle.tmpPane);
            FXMLExampleController.AnchorPanel.getChildren().remove(middle.txt);
            FXMLExampleController.AnchorPanel.getChildren().remove(middle.getLine());
            middle.Remove();
        }
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
