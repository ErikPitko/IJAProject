package Base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Graphics.DrawableObject;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Link implements DrawableObject, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7233283247452423376L;
	private Port inPort;
	private Port outPort;
	private transient Line line;
	private boolean isCycled;
	private static List<Line> cycledLinks = new ArrayList<>();
	public Rectangle tmpPane;
	public Text txt;
	public Link() {
		super();
		line = new Line();
		isCycled = false;
	}
	public Link(Link link) {
		super();
		line = link.line;
		setOutPort(link.outPort);
		setInPort(link.inPort);
		isCycled = link.isCycled;
	}

	public Link(Port inPort, Port outPort) {
		super();
		setInPort(inPort);
		line = new Line();
		setOutPort(outPort);
		isCycled = false;
	}

	public Port getInPort() {
		return inPort;
	}

	public void setInPort(Port inPort) {
		if (this.outPort != null && inPort != null)
			Block.unsetCalculated(outPort.GetBlock());
		this.inPort = inPort;
		if (inPort != null)
			inPort.setLink(this);
	}

	public Port getOutPort() {
		return outPort;
	}

	public void setOutPort(Port outPort) {
		if (this.outPort != null && outPort != null)
			Block.unsetCalculated(outPort.GetBlock());
		this.outPort = outPort;
		if (outPort != null)
			outPort.setLink(this);
	}

	public Line getLine() {
		return line;
	}

	public void Remove()
	{
		outPort = null;
		inPort = null;
	}

	public boolean IsCycled()
	{
		return isCycled;
	}

	public void SetCycled()
	{
		isCycled = true;
		cycledLinks.add(line);
	}

	public void UnSetCycled()
	{
		isCycled = false;
		cycledLinks.remove(line);
	}
	@Override
	public void Draw(AnchorPane pane)
	{
		if(inPort == null||outPort== null)
			return;
		if(!pane.getChildren().contains(line))
		{
			line.setStartX(inPort.Rect.Center().X + Port.PORT_SIZE / 2 + 1);
			line.setStartY(inPort.Rect.Center().Y);
			line.setEndX(outPort.Rect.Center().X - (Port.PORT_SIZE / 2 + 1));
			line.setEndY(outPort.Rect.Center().Y);
			if(isCycled)
				line.setStroke(Color.RED);
			else line.setStroke(Color.BLACK);
			line.setStrokeWidth(3);
			line.setOnMouseEntered(event ->
			{
				tmpPane = new Rectangle();
				tmpPane.setX(event.getX()+1);
				tmpPane.setY(event.getY()+1);
				txt = new Text();
				txt.setText(String.valueOf(inPort.GetBlock().getValue()));
				txt.setFill(Color.BLACK);
				txt.setX(event.getX()+txt.getBoundsInLocal().getWidth()/2);
				txt.setY(event.getY()-txt.getBoundsInLocal().getHeight()/3);
				tmpPane.setX(event.getX()+1);
				tmpPane.setY(event.getY()-txt.getBoundsInLocal().getHeight());
				tmpPane.setHeight(txt.getBoundsInLocal().getHeight());
				tmpPane.setWidth(txt.getBoundsInLocal().getWidth()+20);
				tmpPane.setFill(Color.WHITE);
				tmpPane.setStroke(Color.BLACK);
				pane.getChildren().add(tmpPane);
				pane.getChildren().add(txt);

			});
			line.setOnMouseExited(event ->
			{
				pane.getChildren().remove(tmpPane);
				pane.getChildren().remove(txt);
			});
			pane.getChildren().add(line);
		}
	}

}