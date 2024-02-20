package Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // load the fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/MainWindow.fxml"));
        Parent root= loader.load();
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
