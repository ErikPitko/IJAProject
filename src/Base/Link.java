package Base;

import Graphics.DrawableObject;
import Graphics.LineMesh;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Link implements DrawableObject {
	private LineMesh mesh;
	private Port inPort;
	private Port outPort;
	private Line line;
	public LineMesh getLinkMesh() {
		return mesh;
	}

	public Link() {
		super();
		line = new Line();
	}

	public Link(Port inPort, Port outPort) {
		super();
		setInPort(inPort);
		line = new Line();
		setOutPort(outPort);
	}

	public Link(Port inPort, Port outPort, LineMesh mesh) {
		super();
		this.mesh = mesh;
		line = new Line();
		setInPort(inPort);
		setOutPort(outPort);
	}
	
	public Port getInPort() {
		return inPort;
	}

	public void setInPort(Port inPort) {
		if (this.outPort != null)
			Block.unsetCalculated(outPort.GetBlock());
		this.inPort = inPort;
		inPort.setLink(this);
	}

	public Port getOutPort() {
		return outPort;
	}

	public void setOutPort(Port outPort) {
		if (this.outPort != null)
			Block.unsetCalculated(outPort.GetBlock());
		this.outPort = outPort;
		outPort.setLink(this);
	}

	public Line getLine()
	{
		return line;
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
			line.setFill(Color.BLACK);
			line.setStrokeWidth(3);
			pane.getChildren().add(line);
		}
	}

}