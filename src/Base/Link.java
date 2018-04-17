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
	
	public LineMesh getLinkMesh() {
		return mesh;
	}

	public Link() {
		super();
	}

	public Link(Port inPort, Port outPort) {
		super();
		this.inPort = inPort;
		this.outPort = outPort;
	}

	public Link(Port inPort, Port outPort, LineMesh mesh) {
		super();
		this.mesh = mesh;
		this.inPort = inPort;
		this.outPort = outPort;
	}
	
	public Port getInPort() {
		return inPort;
	}

	public void setInPort(Port inPort) {
		if (this.outPort != null)
			Block.unsetCalculated(outPort.GetBlock());
		this.inPort = inPort;
	}

	public Port getOutPort() {
		return outPort;
	}

	public void setOutPort(Port outPort) {
		if (this.outPort != null)
			Block.unsetCalculated(outPort.GetBlock());
		this.outPort = outPort;
	}

	@Override
	public void Draw(AnchorPane pane)
	{
		if(inPort == null||outPort== null)
			return;
		Line l = new Line();
		l.setStartX(inPort.Rect.Center().X);
		l.setStartY(inPort.Rect.Center().Y);
		l.setEndX(outPort.Rect.Center().X);
		l.setEndY(outPort.Rect.Center().Y);
		l.setFill(Color.BLACK);
		pane.getChildren().add(l);
	}

}