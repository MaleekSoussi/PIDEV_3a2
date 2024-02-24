package test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainFx extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        //load the fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GoBackFront.fxml"));
        //load fxml code in a scene
        Parent root= loader.load();
        //put the fxml file in scene
        Scene scene = new Scene(root);
        //add it to the stage
        stage.setScene(scene);
        // add a title to our window
        stage.setTitle("add ART form");
        ///execute the stage
        stage.show();
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}