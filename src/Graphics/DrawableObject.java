package Graphics;

import javafx.scene.canvas.GraphicsContext;

public interface DrawableObject {
    Point2D Position = null;
    void Draw(GraphicsContext gc);
}