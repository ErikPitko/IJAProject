package Graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.nio.file.Paths;


public class Panel extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Graphics/sample.fxml"));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
