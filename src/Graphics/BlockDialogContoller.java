package Graphics;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Base.Block;
import Base.EBlock;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

	private static Point2D _position;

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
            instance.initStyle(StageStyle.UNDECORATED);
            instance.setResizable(false);
			_position = new Point2D((int)event.getX(),(int)event.getY());
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

	private void InitButt(Button butt, EBlock type)
	{
		ImageView add = new ImageView(new Image(getClass().getResourceAsStream("/Res/"+type.toString()+".png")));
		add.setFitHeight(64);
		add.setFitWidth(64);
		butt.setGraphic(add);
		butt.setPadding(Insets.EMPTY);
		butt.setOnMouseClicked(event->
		{
			Block block = new Block(type,new Rect(_position,100,100));
			Panel.BlockList.add(block);
			block.Draw(FXMLExampleController.AnchorPanel);
			close();
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		InitButt(ADD,EBlock.ADD);
		InitButt(SUB,EBlock.SUB);
		InitButt(MUL,EBlock.MUL);
		InitButt(DIV,EBlock.DIV);
		InitButt(IN,EBlock.IN);
		InitButt(OUT,EBlock.OUT);
	}
	

}
