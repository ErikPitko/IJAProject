package Graphics;

import java.net.URL;
import java.util.ResourceBundle;

import Base.Block;
import Base.EBlock;
import Base.Link;
import Base.Port;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import jdk.nashorn.internal.ir.BlockLexicalContext;
import sun.security.util.PendingException;

public class FXMLExampleController implements Initializable
{
    @FXML
    private AnchorPane anch;

    public static AnchorPane AnchorPanel;
    private Port source;
    private boolean isIn;
    private Point2D startDrag;
    private Block startDragBlock;
    
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
                                            if(Panel.BlockList.get(i).getInPorts().get(y).GetLinks().size() > 0)
                                                source = null;
                                            else if(source.GetBlock() != Panel.BlockList.get(i).getInPorts().get(y).GetBlock())
                                            {
                                            	Block.unsetCalculated(source.GetBlock());
                                            	Block.unsetCalculated(Panel.BlockList.get(i));
                                                Link l2 = new Link(source, Panel.BlockList.get(i).getInPorts().get(y));
                                                l2.Draw(anch);
                                                source = null;
                                            }
                                        }
                                    if(source != null)
                                        if(Panel.BlockList.get(i).GetOutPort().Rect.getX() == ((Rectangle) arg0.getTarget()).getX() && Panel.BlockList.get(i).GetOutPort().Rect.getY() == ((Rectangle) arg0.getTarget()).getY()&& isIn)
                                        {
                                            if(source.GetLinks().size() > 0)
                                                source = null;
                                            else if(source.GetBlock() != Panel.BlockList.get(i).GetOutPort().GetBlock())
                                            {
                                            	Block.unsetCalculated(source.GetBlock());
                                            	Block.unsetCalculated(Panel.BlockList.get(i));
                                                Link l2 = new Link(Panel.BlockList.get(i).GetOutPort(),source);
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
            if ((event.getTarget() instanceof ImageView)) {
                if(((ImageView) event.getTarget()).getFitHeight()!= Port.PORT_SIZE && ((ImageView) event.getTarget()).getFitHeight()!= Port.PORT_SIZE)
                    for (int i = 0;i<Panel.BlockList.size();i++) {
                        if (Panel.BlockList.get(i).getImageView() == event.getTarget())
                        {
                            startDrag = new Point2D((int) event.getX(), (int) event.getY());
                            startDragBlock = Panel.BlockList.get(i);
                        }
                    }
            }
        });
        anch.setOnMouseDragged(event ->
        {
            if(startDragBlock != null)
            {
                double deltaX = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).X;
                double deltaY = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).Y;
                startDragBlock.Move(deltaX, deltaY);
                startDrag = new Point2D((int) event.getX(), (int) event.getY());
            }
        });
        anch.setOnMouseReleased(event ->
        {
            if(startDragBlock != null)
            {
                startDragBlock = null;
            }
        });
    }

}
