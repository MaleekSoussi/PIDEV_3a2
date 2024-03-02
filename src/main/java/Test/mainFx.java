package Test;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontOrBack.fxml"));
        //load fxml code in a scene
        Parent root= loader.load();
        //put the fxml file in scene
        Scene scene = new Scene(root,1300,800);
        //add it to the stage
        stage.setScene(scene);
        // add a title to our window
        stage.setTitle("add Course form");
        ///execute the stage
        stage.show();
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}
