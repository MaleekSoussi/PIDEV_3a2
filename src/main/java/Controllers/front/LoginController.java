package Controllers.front;

import Services.UserService;
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
    private Button facebookButton;

    @FXML
    private Button gmailButton;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signinbutton;

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: to remove the header text
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void gottosignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Signup.fxml"));
            Parent root = loader.load();

            // Get the current stage using the event source
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            // Set the new scene to the stage with the loaded root
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }

    @FXML
    public void login(ActionEvent event) {
        String email = emailField.getText(); // get the email from the email field
        String password = passwordField.getText(); // get the password from the password field

        if (us.login(email, password)) { // Use UserService's login method for authentication
            // Check if the user's account is disabled
            if ("Disabled".equals(UserService.currentlyLoggedInUser.getAccountStatus())) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Your account is disabled.");
                return; // Stop the login process
            }

            if ("UserAdmin".equals(UserService.currentlyLoggedInUser.getRole())) {
                try {
                    // Update the last_login field in the database
                    us.updateLastLoginTimestamp(email);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/Dashboard.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException | SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Loading Error", "Error while loading: " + e.getMessage());
                }
            }
            else {


            try {
                // Update the last_login field in the database
                us.updateLastLoginTimestamp(email);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/MainWindow.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Loading Error", "Error while loading the main window: " + e.getMessage());
            }
            }
        } else {

            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect email or password.");
        }

    }




}
