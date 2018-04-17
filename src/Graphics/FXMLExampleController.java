package Graphics;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Base.Block;
import Base.EBlock;
import Base.Link;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FXMLExampleController implements Initializable
{
	private static Stage BlockDialog;
	
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
    
    private void BlockDialog(MouseEvent event) {
		Parent root;
        try {
        	if (BlockDialog != null)
        		BlockDialog.close();
            root = FXMLLoader.load(getClass().getClassLoader().getResource("Res/blockDialog.fxml"));
            BlockDialog = new Stage();
            BlockDialog.setTitle("");
            BlockDialog.setScene(new Scene(root, 198, 132));
            BlockDialog.setX(Panel.getStage().getX() + event.getX());
            BlockDialog.setY(Panel.getStage().getY() + event.getY());
            BlockDialog.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
    
    public static void onClose() {
    	if(BlockDialog != null)
			BlockDialog.close();
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
        
        anch.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if(arg0.getButton().equals(MouseButton.SECONDARY)) {
					BlockDialog(arg0);
				}
			}
        	
		});
        
    }

}
