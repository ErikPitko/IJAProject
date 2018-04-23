package Graphics;

import Base.Block;
import Base.EBlock;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.ArrayList;


public class Panel extends Application
{
	
	private static Stage stage;
    public static ArrayList<Block> BlockList;
    public static int stepCounter;
	
    public static Stage getStage() {
		return stage;
	}

    public static void ClearAllBlocks()
    {
        int size = Panel.BlockList.size();
        for (int i =0;i<size;i++)
            Panel.BlockList.get(0).DeleteBlock();
    }
    
	@Override
    public void start(Stage primaryStage) throws Exception
    {
        BlockList = new ArrayList<>();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Graphics/sample.fxml"));
        Scene scene = new Scene(root,800,600);
        primaryStage.setTitle("IJA project");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	
        	@Override
        	public void handle(WindowEvent event) {
        		FXMLExampleController.onClose();
        	}
        });
        
        scene.setOnKeyPressed(arg -> {
        	if(arg.getCode() == KeyCode.ENTER) {
        	    Block.stepCounter = Integer.MAX_VALUE;
        		for(int i = 0; i < Panel.BlockList.size(); i++)
        		{
        		    if(Panel.BlockList.get(i).getType() == EBlock.OUT)
        			    Block.compute(Panel.BlockList.get(i));
        		}
        	}
        	else if(arg.getCode() == KeyCode.SPACE)
            {
                for(int i =0 ;i< Panel.BlockList.size();i++)
                {
                    ImageView image = Panel.BlockList.get(i).getImageView();
                    image.setEffect(null);
                    image.setCache(true);
                    image.setCacheHint(CacheHint.SPEED);
                }
                Block outBlock = null;
                for(int i = 0; i < Panel.BlockList.size(); i++)
                {
                    if(Panel.BlockList.get(i).getType() == EBlock.OUT && Panel.BlockList.get(i).getValue() == 0)
                        outBlock = Panel.BlockList.get(i);
                }
                if(outBlock != null) {
                    stepCounter++;
                    Block.unsetCalculated(outBlock);
                    Block.compute(outBlock);
                }
            }
        });
        
        stage = primaryStage;
        primaryStage.show();
    }
	
    public static void main(String[] args) {
        Application.launch(args);
    }
}
