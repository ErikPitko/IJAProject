package Graphics;

import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

public interface DrawableObject {
    Point2D Position = null;

    void Draw(AnchorPane pane);
}