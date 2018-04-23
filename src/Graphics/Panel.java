package Graphics;

import java.util.ArrayList;

import Base.Block;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Panel extends Application
{
	
	private static Stage stage;
    public static ArrayList<Block> BlockList;
	
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
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	
        	@Override
        	public void handle(WindowEvent event) {
        		FXMLExampleController.onClose();
        	}
        });
        
        scene.setOnKeyPressed(arg -> {
        	if(arg.getCode() == KeyCode.ENTER) {
        		for(int i = 0; i < Panel.BlockList.size(); i++) {
        			Block.compute(Panel.BlockList.get(i));
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
