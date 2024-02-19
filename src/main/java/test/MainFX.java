package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // load the fxml file
        System.out.println("print");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddDelivery.fxml"));
        Parent root= loader.load();
        System.out.println("print load");
        Scene scene = new Scene(root);
        // set a scene in stage
        stage.setScene(scene);
        stage.setTitle("VinciApp");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}