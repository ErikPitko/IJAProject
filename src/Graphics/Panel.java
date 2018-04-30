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

import java.util.ArrayList;

import Base.Block;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The Panel class.
 */
public class Panel extends Application {

	/** Main application stage. */
	private static Stage stage;

	/** List of all blocks on scene. */
	public static ArrayList<Block> BlockList;

	/** step counter used in debug mode, increments by one on space press */
	public static int stepCounter;

	/**
	 * Gets the stage.
	 *
	 * @return the stage
	 */
	public static Stage getStage() {
		return stage;
	}

	/**
	 * Clear all blocks from list.
	 */
	public static void ClearAllBlocks() {
		int size = Panel.BlockList.size();
		for (int i = 0; i < size; i++)
			Panel.BlockList.get(0).DeleteBlock();
	}

	/**
	 * The start method is called after the init method has returned.
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		BlockList = new ArrayList<>();
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Graphics/mainWindow.fxml"));
		Scene scene = new Scene(root, 800, 600);
		primaryStage.setTitle("IJA project");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				MainWindowController.onClose();
			}
		});

		stage = primaryStage;
		primaryStage.show();
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}
}
