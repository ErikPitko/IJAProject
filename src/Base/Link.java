package Base;

import Graphics.DrawableObject;
import Graphics.LineMesh;
import javafx.scene.canvas.GraphicsContext;

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
	public void Draw(GraphicsContext gc) {
		
	}

}