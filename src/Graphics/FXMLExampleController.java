package Graphics;

import Base.Block;
import Base.EBlock;
import Base.Link;
import Base.Port;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class FXMLExampleController implements Initializable
{
    @FXML
    private AnchorPane _anchorPanelComponent;
    @FXML
    private MenuItem _openComponent;
    @FXML
    private MenuItem _saveComponent;
    @FXML
    private MenuItem _exitComponent;
    @FXML
    private MenuItem _aboutComponent;
    @FXML
    private Label _errorLog;

    public static AnchorPane AnchorPanel;
    private Port source;
    private boolean isIn;
    private Point2D startDrag;
    private Block startDragBlock;
    private Block blDelete;
    private boolean isBackgroundMove;

    private static final ContextMenu contextMenu = new ContextMenu();
    
    public static void onClose() {
    	BlockDialogContoller.close();
    }

    private void ShowError(Link l2, int i)
    {
        if (Block.isCycled(null, Panel.BlockList.get(i)))
        {
            l2.SetCycled();
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(3000),
                    ae -> _errorLog.setText("")));
            timeline.play();
            _errorLog.setText("Warning: Scheme contains cycle.");
        }else {
            Block.unsetCalculated(source.GetBlock());
            Block.unsetCalculated(Panel.BlockList.get(i));
        }
        l2.Draw(_anchorPanelComponent);
        source = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        AnchorPanel = _anchorPanelComponent;
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
        bl.Draw(_anchorPanelComponent);
        bl1.Draw(_anchorPanelComponent);
        bl2.Draw(_anchorPanelComponent);
        Panel.BlockList.add(bl);
        Panel.BlockList.add(bl1);
        Panel.BlockList.add(bl2);
        _openComponent.setOnAction(event ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open scheme");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All-files", "*.*"),
                    new FileChooser.ExtensionFilter("Scheme", "*.ija")
            );
            File file = fileChooser.showOpenDialog(Panel.getStage());
            //TODO parse loaded file
        });

        _saveComponent.setOnAction(event ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save scheme");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Scheme", "*.ija")
            );
            File file = fileChooser.showSaveDialog(Panel.getStage());
            //TODO save scheme to file
        });

        _aboutComponent.setOnAction(event ->
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText(null);
            alert.setContentText("This program was made by Adam Petráš and Erik Pitko 2018.");
            alert.showAndWait();
        });

        _exitComponent.setOnAction(event ->
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit");
            alert.setHeaderText("Do you really want to quit?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Panel.getStage().close();
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        });


        MenuItem item2 = new MenuItem("Delete");
        item2.setOnAction(event ->
        {
            blDelete.DeleteBlock();
        });
        MenuItem item3 = new MenuItem("Exit");
        item3.setOnAction(event ->
        {
            contextMenu.hide();
        });

        contextMenu.getItems().addAll(/*item1, */item2,item3);

        
        _anchorPanelComponent.setOnMouseClicked(arg0 ->
        {
        	BlockDialogContoller.close();
        	if (arg0.getButton().equals(MouseButton.SECONDARY)) {
                System.out.println(arg0.getTarget());
        		if ((arg0.getTarget() instanceof AnchorPane))
        		{
        			BlockDialogContoller.CreateBlockDialog(new Point2D(arg0.getScreenX(), arg0.getScreenY()),new Point2D(arg0.getX(),arg0.getY()));
        		}
        		else if((arg0.getTarget() instanceof ImageView))
        		{
                    if (((ImageView) arg0.getTarget()).getFitHeight() != Port.PORT_SIZE && ((ImageView) arg0.getTarget()).getFitHeight() != Port.PORT_SIZE)
                    {
                        for (int i = 0; i < Panel.BlockList.size(); i++)
                        {
                            if (Panel.BlockList.get(i).getImageView() == arg0.getTarget())
                            {
                                blDelete = Panel.BlockList.get(i);
                            }
                        }
                    }
                    contextMenu.setX(arg0.getX());
                    contextMenu.setY(arg0.getY());
                    contextMenu.show(((ImageView)arg0.getTarget()),arg0.getScreenX(), arg0.getScreenY());
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
                                            if(source == Panel.BlockList.get(i).getInPorts().get(y))
                                            {
                                                System.out.println("Same Block");
                                                source = null;
                                                break;
                                            }
                                            if(Panel.BlockList.get(i).getInPorts().get(y).GetLinks().size() > 0)
                                                source = null;
                                            else if(source.GetBlock() != Panel.BlockList.get(i).getInPorts().get(y).GetBlock())
                                            {
                                                Link l2 = new Link(source, Panel.BlockList.get(i).getInPorts().get(y));
                                                ShowError(l2,i);
                                            }
                                            else
                                            {
                                                source = null;
                                                break;
                                            }

                                        }
                                    if(source != null)
                                        if(Panel.BlockList.get(i).GetOutPort().Rect.getX() == ((Rectangle) arg0.getTarget()).getX() && Panel.BlockList.get(i).GetOutPort().Rect.getY() == ((Rectangle) arg0.getTarget()).getY()&& isIn)
                                        {
                                            if(source.GetLinks().size() > 0)
                                                source = null;
                                            else if(source.GetBlock() != Panel.BlockList.get(i).GetOutPort().GetBlock())
                                            {
                                                Link l2 = new Link(Panel.BlockList.get(i).GetOutPort(),source);
                                                ShowError(l2,i);
                                            }
                                            else
                                            {
                                                source = null;
                                                break;
                                            }
                                        }
                                }
                            }
                    }
                        else source = null;
                }
                else if((arg0.getTarget() instanceof Line))
                {
                    if(arg0.getClickCount() == 2) {
                        for (int i = 0; i < Panel.BlockList.size(); i++) {
                            for (int a = 0; a < Panel.BlockList.get(i).GetOutPort().GetLinks().size(); a++) {
                                if (Panel.BlockList.get(i).GetOutPort().GetLinks().get(a).getLine() == arg0.getTarget()) {
                                    Panel.BlockList.get(i).GetOutPort().GetLinks().get(a).getOutPort().unSetLink();
                                }
                            }
                        }
                    }
                }
                else if(arg0.getTarget() instanceof ImageView)
                {
                    source = null;
                }
            }
        });

        _anchorPanelComponent.setOnMousePressed(event ->
        {
            if (event.getButton().equals(MouseButton.PRIMARY))
            {
                if ((event.getTarget() instanceof ImageView))
                {
                    if (((ImageView) event.getTarget()).getFitHeight() != Port.PORT_SIZE && ((ImageView) event.getTarget()).getFitHeight() != Port.PORT_SIZE)
                        for (int i = 0; i < Panel.BlockList.size(); i++)
                        {
                            if (Panel.BlockList.get(i).getImageView() == event.getTarget())
                            {
                                startDrag = new Point2D((int) event.getX(), (int) event.getY());
                                startDragBlock = Panel.BlockList.get(i);
                            }
                        }
                }
                if ((event.getTarget() instanceof AnchorPane))
                {
                    startDrag = new Point2D((int) event.getX(), (int) event.getY());
                    isBackgroundMove = true;
                }
            }
        });
        _anchorPanelComponent.setOnMouseDragged(event ->
        {
            if(startDragBlock != null)
            {
                double deltaX = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).X;
                double deltaY = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).Y;
                startDragBlock.Move(deltaX, deltaY);
                startDrag = new Point2D((int) event.getX(), (int) event.getY());
            }
            else if(isBackgroundMove)
            {
                double deltaX = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).X;
                double deltaY = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).Y;
                for (Block blo:Panel.BlockList)
                {
                    blo.Move(deltaX,deltaY);
                }
                startDrag = new Point2D((int) event.getX(), (int) event.getY());
            }
        });
        _anchorPanelComponent.setOnMouseReleased(event ->
        {
            if(startDragBlock != null)
            {
                startDragBlock = null;
            }
            else if(isBackgroundMove)
                isBackgroundMove = false;
        });
    }

}
