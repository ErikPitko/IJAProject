package Graphics;

import java.net.URL;
import java.util.ResourceBundle;

import Base.Block;
import Base.EBlock;
import Base.Link;
import Base.Port;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class FXMLExampleController implements Initializable
{
    @FXML
    private AnchorPane anch;

    public static AnchorPane AnchorPanel;
    private Port source;
    private boolean isIn;
    private Point2D startDrag;
    private Block startDragBlock;
    private Rectangle rectangle;
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
    
    public static void onClose() {
    	BlockDialogContoller.close();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        AnchorPanel = anch;
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
        Panel.BlockList.add(bl);
        Panel.BlockList.add(bl1);
        Panel.BlockList.add(bl2);
        anch.setOnMouseClicked(arg0 ->
        {
        	BlockDialogContoller.close();
        	if (arg0.getButton().equals(MouseButton.SECONDARY)) {
                System.out.println(arg0.getTarget());
        		if ((arg0.getTarget() instanceof AnchorPane))
        		{
        			BlockDialogContoller.CreateBlockDialog(arg0);
        		}
        	}

                if (arg0.getButton().equals(MouseButton.PRIMARY))
                {
                    if ((arg0.getTarget() instanceof Rectangle))
                    {
                        if(((Rectangle)arg0.getTarget()).getWidth()== Port.PORT_SIZE && ((Rectangle) arg0.getTarget()).getHeight()==Port.PORT_SIZE)
                        {
                            if(source == null)
                            {
                                for (int i = 0;i<Panel.BlockList.size();i++)
                                {
                                    for(int y = 0;y<Panel.BlockList.get(i).getInPorts().size();y++)
                                    {
                                        if (Panel.BlockList.get(i).getInPorts().get(y).Rect.getX() == ((Rectangle) arg0.getTarget()).getX() && Panel.BlockList.get(i).getInPorts().get(y).Rect.getY() == ((Rectangle) arg0.getTarget()).getY()) {
                                            source = Panel.BlockList.get(i).getInPorts().get(y);
                                            isIn = true;
                                        }
                                    }
                                    if(source == null)
                                        if(Panel.BlockList.get(i).GetOutPort().Rect.getX() == ((Rectangle) arg0.getTarget()).getX() && Panel.BlockList.get(i).GetOutPort().Rect.getY() == ((Rectangle) arg0.getTarget()).getY()  )
                                        {
                                            source = Panel.BlockList.get(i).GetOutPort();
                                            isIn = false;
                                        }
                                }
                            }
                            else
                            {
                                for (int i = 0;i<Panel.BlockList.size();i++)
                                {
                                    for(int y = 0;y<Panel.BlockList.get(i).getInPorts().size();y++)
                                        if(Panel.BlockList.get(i).getInPorts().get(y).Rect.getX() == ((Rectangle) arg0.getTarget()).getX() && Panel.BlockList.get(i).getInPorts().get(y).Rect.getY() == ((Rectangle) arg0.getTarget()).getY()&& !isIn)
                                        {
                                            if(Panel.BlockList.get(i).getInPorts().get(y).getLink() != null)
                                                source = null;
                                            else if(source.GetBlock() != Panel.BlockList.get(i).getInPorts().get(y).GetBlock())
                                            {
                                                Link l2 = new Link(source, Panel.BlockList.get(i).getInPorts().get(y));
                                                l2.Draw(anch);
                                                source = null;
                                            }
                                        }
                                    if(source != null)
                                        if(Panel.BlockList.get(i).GetOutPort().Rect.getX() == ((Rectangle) arg0.getTarget()).getX() && Panel.BlockList.get(i).GetOutPort().Rect.getY() == ((Rectangle) arg0.getTarget()).getY()&& isIn)
                                        {
                                            if(source.getLink() != null)
                                                source = null;
                                            else if(source.GetBlock() != Panel.BlockList.get(i).GetOutPort().GetBlock())
                                            {
                                                Link l2 = new Link(source, Panel.BlockList.get(i).GetOutPort());
                                                l2.Draw(anch);
                                                source = null;
                                            }
                                        }
                                }


                            }
                    }
                    else source = null;
                    /*
                    if(arg0.getClickCount() == 2)
                    {

                    }
                    */
                }
            }
        });
        anch.setOnMousePressed(event ->
        {
            if ((event.getTarget() instanceof Rectangle)) {
                if(((Rectangle) event.getTarget()).getHeight()!= Port.PORT_SIZE && ((Rectangle) event.getTarget()).getWidth()!= Port.PORT_SIZE)
                    for (int i = 0;i<Panel.BlockList.size();i++) {
                        if (Panel.BlockList.get(i).getRect() == (Rectangle)(event.getTarget())) {
                            rectangle = (Rectangle) event.getTarget();
                            startDrag = new Point2D((int) event.getX(), (int) event.getY());
                            startDragBlock = Panel.BlockList.get(i);
                        }
                    }
            }
        });
        anch.setOnMouseReleased(event ->
        {
            if(startDragBlock != null)
            {
                double deltaX= - Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).X;
                double deltaY = - Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).Y;
                startDragBlock.getRect().setX(startDragBlock.getRect().getX() + deltaX);
                startDragBlock.getRect().setY(startDragBlock.getRect().getY() + deltaY);
                startDragBlock.GetOutPort().Rect.setX(startDragBlock.GetOutPort().Rect.getX()+deltaX);
                startDragBlock.GetOutPort().Rect.setY(startDragBlock.GetOutPort().Rect.getY()+deltaY);
                if(startDragBlock.GetOutPort().getLink()!= null)
                {
                    anch.getChildren().remove(startDragBlock.GetOutPort().getLink().getLine());
                    startDragBlock.GetOutPort().getLink().Draw(anch);
                }
                for (Port inport: startDragBlock.getInPorts())
                {

                    inport.Rect.setX(inport.Rect.getX()+deltaX);
                    inport.Rect.setY(inport.Rect.getY()+deltaY);
                    System.out.println(anch);
                    if(inport.getLink()!= null)
                    {
                        anch.getChildren().remove(inport.getLink().getLine());
                        inport.getLink().Draw(anch);
                    }
                }
                startDragBlock = null;
                System.out.println(Point2D.Vector(new Point2D((int) event.getX(), (int) event.getY()), startDrag));
                //System.out.println(Point2D.Distance(new Point2D((int)event.getgetX(),(int)event.getgetY()),startDrag));
            }
        });
    }

}
