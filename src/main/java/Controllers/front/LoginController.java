package Controllers.front;

import Services.UserService;
import Test.MainFX;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private CheckBox rememberMeCheckbox;

    @FXML
    public void login(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (us.login(email, password)) {

            if (rememberMeCheckbox.isSelected()) {
                us.rememberUser(email, password);
            } else {
                us.clearRememberedUser();
            }

            if ("Disabled".equals(UserService.currentlyLoggedInUser.getAccountStatus())) {
                us.showAlert(Alert.AlertType.ERROR, "Login Failed", "Your account is disabled.");
                return;
            }

            try {
                us.updateLastLoginTimestamp(email);
                String redirectPath = "UserAdmin".equals(UserService.currentlyLoggedInUser.getRole()) ? "/back/Dashboard.fxml" : "/front/MainWindow.fxml";
                us.switchView(MainFX.primaryStage, redirectPath);

            } catch (SQLException e) {
                us.showAlert(Alert.AlertType.ERROR, "Loading Error", "Error while loading: " + e.getMessage());
            }
        } else {
            us.showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect email or password.");
        }
    }


}




