package Controllers.front;

import Services.UserService;
import Test.MainFX;
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
        us.switchView(MainFX.primaryStage, "/front/Login.fxml");
    }

    @FXML
    void signup(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Signup.fxml");
    }

    @FXML
    private Button settingspage;
    private UserService us = new UserService();

    @FXML
    void settings(ActionEvent event) {
            // Replace this with your actual method to check if a user is logged in
            if (UserService.currentlyLoggedInUser==null) {
                // User is not logged in, load the Login view
                us.switchView(MainFX.primaryStage, "/front/Login.fxml");
            } else {
                // User is logged in, load the Settings view
                us.switchView(MainFX.primaryStage, "/front/Settings.fxml");
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
