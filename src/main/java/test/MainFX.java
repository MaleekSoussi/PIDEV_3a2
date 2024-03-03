package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class MainFX extends Application{
    @Override
    public void start(Stage stage) throws Exception
    {
        //load the fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontOrBack.fxml"));
        //load fxml code in a scene
        Parent root= loader.load();
        //put the fxml file in scene
        Scene scene = new Scene(root,1300,800);
        //add it to the stage
        stage.setScene(scene);
        // add a title to our window
        stage.setTitle("add Event form");
        ///execute the stage
        stage.show();
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}
