package Graphics;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Base.Block;
import Base.EBlock;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BlockDialogContoller implements Initializable{

	private static Stage instance;
	private EBlock buttSelected;
	
	@FXML
	private ToggleButton ADD;
	@FXML
	private ToggleButton SUB;
	@FXML
	private ToggleButton MUL;
	@FXML
	private ToggleButton DIV;
	@FXML
	private ToggleButton IN;
	@FXML
	private ToggleButton OUT;
	@FXML
	private Button Apply;
	@FXML
	private Button Cancel;
	@FXML
	private Label ValueLabel;
	@FXML
	private TextField Value;
	@FXML
	private Slider InputSlider;

	private static Point2D _position;

	public static void CreateBlockDialog(Point2D position,Point2D clickedPosition) {
		close();
		Parent root;
        try {
			root = FXMLLoader.load(BlockDialogContoller.class.getClassLoader().getResource("Graphics/blockDialog.fxml"));
			instance = new Stage();
			instance.setTitle("");
			instance.setScene(new Scene(root));
			instance.setX(position.X);
			instance.setY(position.Y);
			instance.initStyle(StageStyle.UNDECORATED);
			instance.setResizable(false);
			_position = clickedPosition;
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

	private void InitButt(ToggleButton butt, EBlock type)
	{
		ImageView add = new ImageView(new Image(getClass().getResourceAsStream("/Res/"+type.toString()+".png")));
		add.setFitHeight(64);
		add.setFitWidth(64);
		butt.setGraphic(add);
		butt.setPadding(Insets.EMPTY);
		butt.setOnMouseClicked(event->{
			if (type == EBlock.IN) {
				Value.setVisible(true);
				ValueLabel.setVisible(true);
				InputSlider.setDisable(true);
				InputSlider.setValue(0);
			}else {
				Value.setVisible(false);
				ValueLabel.setVisible(false);
				InputSlider.setDisable(false);
				InputSlider.setValue(2);
			}
			buttSelected = type;
		});
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		InitButt(ADD,EBlock.ADD);
		buttSelected = EBlock.ADD;
		InitButt(SUB,EBlock.SUB);
		InitButt(MUL,EBlock.MUL);
		InitButt(DIV,EBlock.DIV);
		InitButt(IN,EBlock.IN);
		InitButt(OUT,EBlock.OUT);
		
		Value.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d.*")) {
		            Value.setText(newValue.replaceAll("[^\\d.]", ""));
		        }
		    }
		});

		Cancel.setOnMouseClicked(event ->
		{
			close();
		});
		
		Apply.setOnMouseClicked(event->{
			if (buttSelected != null) {
				Block block;
				if (buttSelected == EBlock.IN)
					block = new Block(buttSelected, new Rect(_position,100,100), Double.parseDouble(Value.getText()));
				else 
					block = new Block(buttSelected, new Rect(_position,100,100));
				for(int i = 0; i < InputSlider.getValue(); i++)
					block.genInPort();
				Panel.BlockList.add(block);
				block.Draw(FXMLExampleController.AnchorPanel);
				close();
			}
		});
	}
	

}
