package Graphics;

import Base.Block;
import Base.EBlock;
import Base.Link;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLExampleController implements Initializable
{
    @FXML
    private AnchorPane anch;
    private void drawDShape(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(100, 100);
        gc.bezierCurveTo(0, 0, 150, 150, 75, 150);
        gc.closePath();
    }
    private void drawLines(GraphicsContext gc) {

        gc.beginPath();
        gc.moveTo(30.5, 30.5);
        gc.lineTo(150.5, 30.5);
        gc.lineTo(150.5, 150.5);
        gc.lineTo(30.5, 30.5);
        gc.stroke();
    }
    private Point2D mouseDrag;
    private Point2D lineStartPosit;

    private void DrawLine(GraphicsContext gc, MouseEvent event)
    {
        if(lineStartPosit == null)
            lineStartPosit = new Point2D((int) event.getX(),(int)event.getY());
        else
        {
            gc.beginPath();
            gc.moveTo(lineStartPosit.X,lineStartPosit.Y);
            gc.lineTo((int) event.getX(),(int)event.getY());
            gc.stroke();
            lineStartPosit = null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Block bl = new Block(EBlock.ADD,new Rect(100,100,200,200));
        bl.genInPort();
        bl.genInPort();
        bl.genInPort();
        bl.genInPort();
        bl.genInPort();
        bl.genInPort();
        Block bl1 = new Block(EBlock.ADD,new Rect(350,350,100,100));
        bl1.genInPort();
        bl1.genInPort();
        bl1.genInPort();
        Link l = new Link();
        l.setInPort(bl.GetOutPort());
        l.setOutPort(bl1.getInPorts().get(0));
        bl.GetOutPort().setLink(l);
        bl1.getInPorts().get(0).setLink(l);
        Block bl2 = new Block(EBlock.ADD,new Rect(500,100,100,100));
        bl2.genInPort();
        bl2.genInPort();
        bl2.genInPort();
        Link ll = new Link();
        ll.setInPort(bl1.GetOutPort());
        ll.setOutPort(bl2.getInPorts().get(0));
        bl1.GetOutPort().setLink(ll);
        bl2.getInPorts().get(0).setLink(ll);
        Link lll = new Link();
        lll.setInPort(bl.GetOutPort());
        lll.setOutPort(bl2.getInPorts().get(1));
        bl.GetOutPort().setLink(lll);
        bl2.getInPorts().get(1).setLink(lll);
        bl.Draw(anch);
        bl1.Draw(anch);
        bl2.Draw(anch);
        
    }

}
