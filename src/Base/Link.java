package Base;

import Graphics.DrawableObject;
import Graphics.LineMesh;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class Link implements DrawableObject {
	private LineMesh mesh;
	private Port inPort;
	private Port outPort;
	private Line line;
	private boolean isCycled;
	private static List<Line> cycledLinks = new ArrayList<>();

	public LineMesh getLinkMesh() {
		return mesh;
	}

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

	public Link(Port inPort, Port outPort, LineMesh mesh) {
		super();
		this.mesh = mesh;
		line = new Line();
		setInPort(inPort);
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
			pane.getChildren().add(line);
		}
	}

}