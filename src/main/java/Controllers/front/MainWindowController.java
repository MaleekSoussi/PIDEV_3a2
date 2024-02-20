package Controllers.front;

import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowController {

    @FXML
    private Button Signupbutton;

    @FXML
    private Button loginButton;



    @FXML
    void login(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Login.fxml"));
            Parent root = loader.load();

            // Get the current stage using the event source
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Set the new scene to the stage with the loaded root
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.println("error:" + e.getMessage());
        }
    }

    @FXML
    void signup(ActionEvent event) {
        try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Signup.fxml"));
                Parent root = loader.load();

                // Get the current stage using the event source
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                // Set the new scene to the stage with the loaded root
                stage.setScene(new Scene(root));
                stage.show();

        } catch (IOException e) {
            System.out.println("error:" + e.getMessage());
        }
    }

    @FXML
    private Button settingspage;
    private UserService us = new UserService();

    @FXML
    void settings(ActionEvent event) {
        try {
            // Replace this with your actual method to check if a user is logged in
            if (UserService.currentlyLoggedInUser==null) {
                // User is not logged in, load the Login view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Login.fxml"));
                Parent root = loader.load();

                // Get the current stage using the event source
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                // Set the new scene to the stage with the loaded root
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                // User is logged in, load the Settings view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Settings.fxml"));
                Parent root = loader.load();

                // Get the current stage using the event source
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                // Set the new scene to the stage with the loaded root
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (IOException e) {
            System.out.println("error:"+e.getMessage());
        }
    }



    public void initialize() {
        // Check if a user is logged in
        if (UserService.currentlyLoggedInUser != null) {
            // User is logged in
            loginButton.setVisible(false);
            Signupbutton.setVisible(false);

            loginButton.setManaged(false);
            Signupbutton.setManaged(false);
        }
    }

}
