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
        us.switchView(MainFX.primaryStage, "/front/Transition.fxml");
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

            if (UserService.currentlyLoggedInUser==null) {
                us.switchView(MainFX.primaryStage, "/front/Transition.fxml");
            } else {
                us.switchView(MainFX.primaryStage, "/front/Settings.fxml");
            }
    }



    public void initialize() {
        if (UserService.currentlyLoggedInUser != null) {
            loginButton.setVisible(false);
            Signupbutton.setVisible(false);
        }
    }

}
