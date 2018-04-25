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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Base.Block;
import Base.EBlock;
import Base.Link;
import Base.LoadManager;
import Base.Port;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 * The MainWindow controller class.
 */
public class MainWindowController implements Initializable {

	/** The anchor panel component. */
	@FXML
	private AnchorPane _anchorPanelComponent;

	/** The open menu-item component. */
	@FXML
	private MenuItem _openComponent;

	/** The save menu-item component. */
	@FXML
	private MenuItem _saveComponent;

	/** The exit menu-item component. */
	@FXML
	private MenuItem _exitComponent;

	/** The about menu-item component. */
	@FXML
	private MenuItem _aboutComponent;

	/** The clear menu-item component. */
	@FXML
	private MenuItem _clearComponent;

	/** The error notification panel component. */
	@FXML
	private Label _errorLog;

	/** The Anchor panel. */
	public static AnchorPane AnchorPanel;

	/** The source port. */
	private Port source;

	/** The is-in component flag. */
	private boolean isIn;

	/** The start drag position. */
	private Point2D startDrag;

	/** The block to be dragged. */
	private Block startDragBlock;

	/** The block to be deleted. */
	private Block blDelete;

	/** The background move flag. */
	private boolean isBackgroundMove;

	/** The block to be resized. */
	private Block resizeBlock;

	/** The Constant contextMenu. */
	private static final ContextMenu contextMenu = new ContextMenu();

	/**
	 * Closes Block dialog before closing main window
	 */
	public static void onClose() {
		BlockDialogContoller.close();
	}

	/**
	 * Checks link for loop and prints it in notification bar.
	 *
	 * @param l2
	 *            the link to be checked
	 * @param i
	 *            the index of the block in loop
	 */
	private void ShowError(Link l2, int i) {
		if (Block.isCycled(null, Panel.BlockList.get(i))) {
			l2.SetCycled();
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), ae -> _errorLog.setText("")));
			timeline.play();
			_errorLog.setText("Warning: Scheme contains cycle.");
		} else {
			Block.unsetCalculated(source.GetBlock());
			Block.unsetCalculated(Panel.BlockList.get(i));
		}
		l2.Draw(_anchorPanelComponent);
		source = null;
	}

	/**
	 * Initializes scene at the start of the application
	 * 
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnchorPanel = _anchorPanelComponent;
		Block bl = new Block(EBlock.ADD, new Rect(100, 100, 200, 200));
		bl.genInPort();
		bl.genInPort();
		bl.genInPort();
		bl.genInPort();
		bl.genInPort();
		bl.genInPort();
		Block bl1 = new Block(EBlock.ADD, new Rect(350, 350, 100, 100));
		bl1.genInPort();
		bl1.genInPort();
		bl1.genInPort();
		Link l = new Link();
		l.setInPort(bl.GetOutPort());
		l.setOutPort(bl1.getInPorts().get(0));
		bl.GetOutPort().setLink(l);
		bl1.getInPorts().get(0).setLink(l);
		Block bl2 = new Block(EBlock.ADD, new Rect(500, 100, 100, 100));
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
		Block bl3 = new Block(EBlock.IN, new Rect(0, 50, 100, 100), 10);
		new Link(bl3.GetOutPort(), bl.getInPorts().get(0));
		Block bl4 = new Block(EBlock.OUT, new Rect(650, 100, 100, 100));
		bl4.genInPort();
		new Link(bl2.GetOutPort(), bl4.getInPorts().get(0));
		bl.Draw(_anchorPanelComponent);
		bl1.Draw(_anchorPanelComponent);
		bl2.Draw(_anchorPanelComponent);
		bl3.Draw(_anchorPanelComponent);
		bl4.Draw(_anchorPanelComponent);
		Panel.BlockList.add(bl);
		Panel.BlockList.add(bl1);
		Panel.BlockList.add(bl2);
		Panel.BlockList.add(bl3);
		Panel.BlockList.add(bl4);
		_openComponent.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open scheme");
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All-files", "*.*"),
					new FileChooser.ExtensionFilter("Scheme", "*.ija"));
			File file = fileChooser.showOpenDialog(Panel.getStage());
			try {
				LoadManager.loadScene(file);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		_saveComponent.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save scheme");
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Scheme", "*.ija"));
			File file = fileChooser.showSaveDialog(Panel.getStage());

			try {
				LoadManager.saveScene(Panel.BlockList, file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		_aboutComponent.setOnAction(event -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText(null);
			alert.setContentText("This program was made by Adam Petráš and Erik Pitko in 2018.");
			alert.showAndWait();
		});
		_clearComponent.setOnAction(event -> {
			Panel.ClearAllBlocks();

		});

		_exitComponent.setOnAction(event -> {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Exit");
			alert.setHeaderText("Do you really want to quit?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				Panel.getStage().close();
			} else {
				// ... user chose CANCEL or closed the dialog
			}
		});

		MenuItem item1 = new MenuItem("Edit");
		item1.setOnAction(event -> {
			BlockDialogContoller.CreateBlockDialog(new Point2D(contextMenu.getX(), contextMenu.getY()),
					blDelete.getRect().Position(), blDelete);
		});
		MenuItem item2 = new MenuItem("Delete");
		item2.setOnAction(event -> {
			blDelete.DeleteBlock();
		});
		MenuItem item3 = new MenuItem("Exit");
		item3.setOnAction(event -> {
			contextMenu.hide();
		});

		contextMenu.getItems().addAll(item1, item2, item3);

		_anchorPanelComponent.setOnMouseClicked(arg0 -> {
			BlockDialogContoller.close();
			if (arg0.getButton().equals(MouseButton.SECONDARY)) {
				System.out.println(arg0.getTarget());
				if ((arg0.getTarget() instanceof AnchorPane)) {
					BlockDialogContoller.CreateBlockDialog(new Point2D(arg0.getScreenX(), arg0.getScreenY()),
							new Point2D(arg0.getX(), arg0.getY()), null);
				} else if ((arg0.getTarget() instanceof ImageView)) {
					if (((ImageView) arg0.getTarget()).getFitHeight() != Port.PORT_SIZE
							&& ((ImageView) arg0.getTarget()).getFitHeight() != Port.PORT_SIZE) {
						for (int i = 0; i < Panel.BlockList.size(); i++) {
							if (Panel.BlockList.get(i).getImageView() == arg0.getTarget()) {
								blDelete = Panel.BlockList.get(i);
							}
						}
					}
					contextMenu.setX(arg0.getX());
					contextMenu.setY(arg0.getY());
					contextMenu.show(((ImageView) arg0.getTarget()), arg0.getScreenX(), arg0.getScreenY());
				}
			}

			if (arg0.getButton().equals(MouseButton.PRIMARY)) {
				if ((arg0.getTarget() instanceof Rectangle)) {
					if (((Rectangle) arg0.getTarget()).getWidth() == Port.PORT_SIZE
							&& ((Rectangle) arg0.getTarget()).getHeight() == Port.PORT_SIZE) {
						if (source == null) {
							for (int i = 0; i < Panel.BlockList.size(); i++) {
								for (int y = 0; y < Panel.BlockList.get(i).getInPorts().size(); y++) {
									if (Panel.BlockList.get(i).getInPorts().get(y).Rect
											.getX() == ((Rectangle) arg0.getTarget()).getX()
											&& Panel.BlockList.get(i).getInPorts().get(y).Rect
													.getY() == ((Rectangle) arg0.getTarget()).getY()) {
										source = Panel.BlockList.get(i).getInPorts().get(y);
										isIn = true;
									}
								}
								if (source == null)
									if (Panel.BlockList.get(i).GetOutPort() != null)
										if (Panel.BlockList.get(i).GetOutPort().Rect
												.getX() == ((Rectangle) arg0.getTarget()).getX()
												&& Panel.BlockList.get(i).GetOutPort().Rect
														.getY() == ((Rectangle) arg0.getTarget()).getY()) {
											source = Panel.BlockList.get(i).GetOutPort();
											isIn = false;
										}
							}
						} else {
							for (int i = 0; i < Panel.BlockList.size(); i++) {

								for (int y = 0; y < Panel.BlockList.get(i).getInPorts().size(); y++)
									if (Panel.BlockList.get(i).getInPorts().get(y).Rect
											.getX() == ((Rectangle) arg0.getTarget()).getX()
											&& Panel.BlockList.get(i).getInPorts().get(y).Rect
													.getY() == ((Rectangle) arg0.getTarget()).getY()
											&& !isIn) {
										if (source == Panel.BlockList.get(i).getInPorts().get(y)) {
											System.out.println("Same Block");
											source = null;
											break;
										}
										if (Panel.BlockList.get(i).getInPorts().get(y).GetLinks().size() > 0)
											source = null;
										else if (source.GetBlock() != Panel.BlockList.get(i).getInPorts().get(y)
												.GetBlock()) {
											Link l2 = new Link(source, Panel.BlockList.get(i).getInPorts().get(y));
											ShowError(l2, i);
										} else {
											source = null;
											break;
										}

									}
								if (source != null)
									if (Panel.BlockList.get(i).GetOutPort() != null)
										if (Panel.BlockList.get(i).GetOutPort().Rect
												.getX() == ((Rectangle) arg0.getTarget()).getX()
												&& Panel.BlockList.get(i).GetOutPort().Rect
														.getY() == ((Rectangle) arg0.getTarget()).getY()
												&& isIn) {
											if (source.GetLinks().size() > 0)
												source = null;
											else if (source.GetBlock() != Panel.BlockList.get(i).GetOutPort()
													.GetBlock()) {
												Link l2 = new Link(Panel.BlockList.get(i).GetOutPort(), source);
												ShowError(l2, i);
											} else {
												source = null;
												break;
											}
										}
							}
						}
					} else
						source = null;
				} else if ((arg0.getTarget() instanceof Line)) {
					if (arg0.getClickCount() == 2) {
						for (int i = 0; i < Panel.BlockList.size(); i++) {
							if (Panel.BlockList.get(i).GetOutPort() != null)
								for (int a = 0; a < Panel.BlockList.get(i).GetOutPort().GetLinks().size(); a++) {
									if (Panel.BlockList.get(i).GetOutPort().GetLinks().get(a).getLine() == arg0
											.getTarget()) {
										Panel.BlockList.get(i).GetOutPort().GetLinks().get(a).getOutPort().unSetLink();
									}
								}
						}
					}
				} else if (arg0.getTarget() instanceof ImageView) {
					source = null;
				}
			}
		});

		_anchorPanelComponent.setOnMousePressed(event -> {
			if (event.getButton().equals(MouseButton.PRIMARY)) {
				if ((event.getTarget() instanceof ImageView)) {
					if (((ImageView) event.getTarget()).getFitHeight() != Port.PORT_SIZE
							&& ((ImageView) event.getTarget()).getFitHeight() != Port.PORT_SIZE)
						for (int i = 0; i < Panel.BlockList.size(); i++) {
							if (Panel.BlockList.get(i).getImageView() == event.getTarget()) {
								startDrag = new Point2D((int) event.getX(), (int) event.getY());
								startDragBlock = Panel.BlockList.get(i);
							}
						}
				} else if ((event.getTarget() instanceof AnchorPane)) {
					startDrag = new Point2D((int) event.getX(), (int) event.getY());
					isBackgroundMove = true;
				} else if ((event.getTarget() instanceof Rectangle)) {
					if (((Rectangle) event.getTarget()).getWidth() == 8
							&& ((Rectangle) event.getTarget()).getHeight() == 8) {
						for (Block tmpBlock : Panel.BlockList) {
							if (tmpBlock.GetResizeRect() == (Rectangle) event.getTarget()) {
								resizeBlock = tmpBlock;
								startDrag = new Point2D((int) event.getX(), (int) event.getY());
							}
						}
					}
				}
			}
		});
		_anchorPanelComponent.setOnMouseDragged(event -> {
			if (startDragBlock != null) {
				double deltaX = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).X;
				double deltaY = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).Y;
				startDragBlock.Move(deltaX, deltaY);
				startDrag = new Point2D((int) event.getX(), (int) event.getY());
			} else if (isBackgroundMove) {
				double deltaX = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).X;
				double deltaY = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).Y;
				for (Block blo : Panel.BlockList) {
					blo.Move(deltaX, deltaY);
				}
				startDrag = new Point2D((int) event.getX(), (int) event.getY());
			} else if (resizeBlock != null) {
				double deltaX = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).X;
				double deltaY = -Point2D.Vector(new Point2D(event.getX(), event.getY()), startDrag).Y;
				resizeBlock.Resize(deltaX, deltaY);
				startDrag = new Point2D((int) event.getX(), (int) event.getY());
			}
		});
		_anchorPanelComponent.setOnMouseReleased(event -> {
			if (startDragBlock != null) {
				startDragBlock = null;
			} else if (isBackgroundMove)
				isBackgroundMove = false;
		});
	}

}
