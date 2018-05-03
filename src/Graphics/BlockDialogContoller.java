/*******************************************************************************
 *
 * VUT FIT Brno - IJA project BlockDiagram
 *
 * Copyright (C) 2018, Adam Petras (xpetra19)
 * Copyright (C) 2018, Erik Pitko (xpitko00)
 * 
 * Contributors: 
 * 		Adam Petras - GUI, base application implementation, tests
 * 		Erik Pitko - base application implementation, Doxygen doc, tests, save/load scene
 * 
 ******************************************************************************/
package Graphics;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Base.Block;
import Base.EBlock;
import Base.Link;
import Base.Port;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The BlockDialogContoller class.
 */
public class BlockDialogContoller implements Initializable {

	/** The instance of block dialog window. */
	private static Stage instance;

	/** The selected button. */
	private EBlock buttSelected;

	/** The add toggle button. */
	@FXML
	private ToggleButton ADD;

	/** The sub toggle button. */
	@FXML
	private ToggleButton SUB;

	/** The mul toggle button. */
	@FXML
	private ToggleButton MUL;

	/** The div toggle button. */
	@FXML
	private ToggleButton DIV;

	/** The input toggle button. */
	@FXML
	private ToggleButton IN;

	/** The output toggle button. */
	@FXML
	private ToggleButton OUT;

	/** The Apply button. */
	@FXML
	private Button Apply;

	/** The Cancel button. */
	@FXML
	private Button Cancel;

	/** The Value label. */
	@FXML
	private Label ValueLabel;

	/** The Value text field. */
	@FXML
	private TextField Value;

	/** The Input slider. */
	@FXML
	private Slider InputSlider;

	/** The position. */
	private static Point2D _position;

	/** The block to be edited */
	private static Block editBlock;

	/**
	 * Creates the block dialog window.
	 *
	 * @param position
	 *            the position of created window
	 * @param clickedPosition
	 *            the clicked position
	 * @param editBlock
	 *            the block to be edited
	 */
	public static void CreateBlockDialog(Point2D position, Point2D clickedPosition, Block editBlock) {
		close();
		BlockDialogContoller.editBlock = editBlock;
		Parent root;
		try {
			root = FXMLLoader
					.load(BlockDialogContoller.class.getClassLoader().getResource("Graphics/blockDialog.fxml"));
			instance = new Stage();
			instance.setTitle("");
			instance.setScene(new Scene(root));
			instance.setX(position.X);
			instance.setY(position.Y);
			instance.initStyle(StageStyle.UNDECORATED);
			instance.setResizable(false);
			_position = clickedPosition;
			instance.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Closes dialog window
	 */
	public static void close() {
		if (instance != null) {
			instance.close();
		}
	}

	/**
	 * Inits given button.
	 *
	 * @param butt
	 *            which button to init
	 * @param type
	 *            the type of button
	 */
	private void InitButt(ToggleButton butt, EBlock type) {
		ImageView add = new ImageView(new Image(getClass().getResourceAsStream("/Res/" + type.toString() + ".png")));
		add.setFitHeight(64);
		add.setFitWidth(64);
		butt.setGraphic(add);
		butt.setPadding(Insets.EMPTY);
		butt.setOnMouseClicked(event -> {
			if (type == EBlock.IN) {
				Value.setVisible(true);
				ValueLabel.setVisible(true);
				InputSlider.setDisable(true);
				InputSlider.setValue(0);
			} else if (type == EBlock.OUT)
			{
				Value.setVisible(false);
				ValueLabel.setVisible(false);
				InputSlider.setDisable(true);
				InputSlider.setValue(1);
			}
			else
				{
				Value.setVisible(false);
				ValueLabel.setVisible(false);
				InputSlider.setDisable(false);
				InputSlider.setValue(2);
			}
			buttSelected = type;
		});
	}

	/*
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		InitButt(ADD, EBlock.ADD);
		buttSelected = EBlock.ADD;
		InitButt(SUB, EBlock.SUB);
		InitButt(MUL, EBlock.MUL);
		InitButt(DIV, EBlock.DIV);
		InitButt(IN, EBlock.IN);
		InitButt(OUT, EBlock.OUT);

		if (editBlock != null)
			Apply.setText("Edit");

		Value.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("^(\\d+(\\.?)\\d*)$")) {
					if (newValue.length() > 0)
						Value.setText(newValue.substring(0, newValue.length() - 1));
				}
			}
		});

		Cancel.setOnMouseClicked(event -> {
			close();
		});

		Apply.setOnMouseClicked(event -> {
			if (buttSelected != null) {
				Block block;
				if (buttSelected == EBlock.IN)
					block = new Block(buttSelected, new Rect(_position, 100, 100), Double.parseDouble(Value.getText()));
				else {
					double sizey = InputSlider.getValue() * (Port.PORT_SIZE + 5);
					if (sizey < Block.MINBLOCKSIZE)
						sizey = Block.MINBLOCKSIZE;
					block = new Block(buttSelected, new Rect(_position, sizey, sizey));
				}
				for (int i = 0; i < InputSlider.getValue(); i++)
					block.genInPort();

				if (editBlock != null) {
					if (editBlock.GetOutPort() != null)
						for (int i = 0; i < editBlock.GetOutPort().GetLinks().size(); i++) {
							new Link(block.GetOutPort(), editBlock.GetOutPort().GetLinks().get(i).getOutPort());
						}
					for (int i = 0; i < editBlock.getInPorts().size(); i++) {
						if (i == block.getInPorts().size())
							break;
						for (int j = 0; j < editBlock.getInPorts().get(i).GetLinks().size(); j++) {
							new Link(editBlock.getInPorts().get(i).GetLinks().get(j).getInPort(),
									block.getInPorts().get(i));
						}

					}
					editBlock.DeleteBlock();
				}
				Panel.BlockList.add(block);
				block.Draw(MainWindowController.AnchorPanel);
				block.Move(1, 0);
				block.Move(-1, 0);
				close();
			}
		});
	}

}
