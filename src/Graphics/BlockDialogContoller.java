package Graphics;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BlockDialogContoller implements Initializable{
	
	private static Stage instance;
	
	@FXML
	private Button ADD;
	@FXML
	private Button SUB;
	@FXML
	private Button MUL;
	@FXML
	private Button DIV;
	@FXML
	private Button IN;
	@FXML
	private Button OUT;

	public static void CreateBlockDialog(MouseEvent event) {
		close();
		Parent root;
        try {
            root = FXMLLoader.load(BlockDialogContoller.class.getClassLoader().getResource("Graphics/blockDialog.fxml"));
            instance = new Stage();
            instance.setTitle("");
            instance.setScene(new Scene(root, 198, 132));
            instance.setX(Panel.getStage().getX() + event.getX());
            instance.setY(Panel.getStage().getY() + event.getY());
            instance.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
	}

	public static void close() {
		if (instance != null) {
			instance.close();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ADD.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/Res/ADD.png"))));
	}
	

}
