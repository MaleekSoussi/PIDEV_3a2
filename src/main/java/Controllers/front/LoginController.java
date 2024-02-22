package Controllers.front;

import Services.UserService;
import Test.MainFX;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController{

    private final UserService us = new UserService();
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    

    @FXML
    void gottosignup(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Signup.fxml");
    }

    @FXML
    public void login(ActionEvent event) {
        String email = emailField.getText(); // get the email from the email field
        String password = passwordField.getText(); // get the password from the password field

        if (us.login(email, password)) { // Use UserService's login method for authentication
            // Check if the user's account is disabled
            if ("Disabled".equals(UserService.currentlyLoggedInUser.getAccountStatus())) {
                 us.showAlert(Alert.AlertType.ERROR, "Login Failed", "Your account is disabled.");
                return; // Stop the login process
            }

            if ("UserAdmin".equals(UserService.currentlyLoggedInUser.getRole())) {
                try {
                    // Update the last_login field in the database
                    us.updateLastLoginTimestamp(email);

                    us.switchView(MainFX.primaryStage, "/back/Dashboard.fxml");

                } catch (SQLException e) {
                     us.showAlert(Alert.AlertType.ERROR, "Loading Error", "Error while loading: " + e.getMessage());
                }
            }
            else {


            try {
                // Update the last_login field in the database
                us.updateLastLoginTimestamp(email);

                us.switchView(MainFX.primaryStage, "/front/MainWindow.fxml");

            } catch (SQLException e) {
                 us.showAlert(Alert.AlertType.ERROR, "Loading Error", "Error while loading the main window: " + e.getMessage());
            }
            }
        } else {

             us.showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect email or password.");
        }

    }




}
