package Graphics;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.*;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLExampleController implements Initializable
{
    @FXML
    private Canvas canvas;
    @FXML
    private Label coordinates;
    private void drawDShape(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(100, 100);
        gc.bezierCurveTo(0, 0, 150, 150, 75, 150);
        gc.closePath();
    }
    private void moveCanvas(int x, int y) {
        canvas.setTranslateX(x);
        canvas.setTranslateY(y);
    }
    private void drawLines(GraphicsContext gc) {

        gc.beginPath();
        gc.moveTo(30.5, 30.5);
        gc.lineTo(150.5, 30.5);
        gc.lineTo(150.5, 150.5);
        gc.lineTo(30.5, 30.5);
        gc.stroke();
    }
    private Point2D mouseDrag;
    private Point2D lineStartPosit;

    private void DrawLine(GraphicsContext gc, MouseEvent event)
    {
        if(lineStartPosit == null)
            lineStartPosit = new Point2D((int) event.getX(),(int)event.getY());
        else
        {
            gc.beginPath();
            gc.moveTo(lineStartPosit.X,lineStartPosit.Y);
            gc.lineTo((int) event.getX(),(int)event.getY());
            gc.stroke();
            lineStartPosit = null;
        }
    }

    private void DrawRect(GraphicsContext gc, MouseEvent event)
    {
        if(lineStartPosit == null)
            lineStartPosit = new Point2D((int) event.getX(),(int)event.getY());
        else
        {
            gc.beginPath();
            gc.moveTo(lineStartPosit.X,lineStartPosit.Y);
            gc.lineTo((int) event.getX(),lineStartPosit.Y);
            gc.lineTo((int) event.getX(),(int)event.getY());
            gc.lineTo((int) lineStartPosit.X,(int)event.getY());
            gc.lineTo((int) lineStartPosit.X,lineStartPosit.Y);
            //gc.lineTo((int) event.getX(),(int)event.getY());

            gc.stroke();
            lineStartPosit = null;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        drawDShape(gc);
        drawLines(gc);
        mouseDrag = new Point2D(0,0);
        canvas.setOnMouseClicked(event -> {
            coordinates.setText("X: "+event.getX()+" Y: "+event.getY());
            DrawLine(gc,event);
        });
        canvas.setOnMousePressed((event -> {
            mouseDrag = new Point2D((int) event.getX(),(int)event.getY());
            System.out.println(mouseDrag);
            System.out.println("DRAG");
        }));
        canvas.setOnMouseDragged((event -> {

        }));
        canvas.setOnMouseReleased(event -> {
            mouseDrag = mouseDrag.add(new Point2D((int) event.getX(), (int) event.getY()));
            //moveCanvas(mouseStartDrag.X, mouseStartDrag.Y);
            System.out.println(mouseDrag);
            System.out.println("DROP");
        });
    }

}
