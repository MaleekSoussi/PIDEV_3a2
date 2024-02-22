package Controllers.front;

import Models.Users;
import Services.UserService;
import Test.MainFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.stage.Stage;


public class SignupController{

    private final UserService us = new UserService();
    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;


    @FXML
    public void clicksignup(ActionEvent event) {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String name = emailField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = (String) roleComboBox.getValue();


        if (firstName.isEmpty()) {
            us.showAlert(Alert.AlertType.ERROR, "Form Error", "First name is required.");
            return;
        }

        // Last Name validation
        if (lastName.isEmpty()) {
            us.showAlert(Alert.AlertType.ERROR, "Form Error", "Last name is required.");
            return;
        }

        // Email validation
        if (email.isEmpty() || !email.matches("^[\\w.-]+@esprit\\.tn$")) {
            us.showAlert(Alert.AlertType.ERROR, "Invalid Email", "Email must not be empty & end with @esprit.tn");
            return;
        }

        // Password validation
        if (password == null || password.length() < 3) {
             us.showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password must be at least 3 characters long");
            return;
        }

        // Role selection validation
        if (role == null || role.isEmpty()) {
             us.showAlert(Alert.AlertType.ERROR, "Invalid Role", "You must select a role");
            return;
        }

        try {
            Users newUser = new Users(); // Assuming Users is your model class
            newUser.setFirstName(firstNameField.getText());
            newUser.setLastName(lastNameField.getText());
            newUser.setPassword(passwordField.getText());
            newUser.setEmailAddress(emailField.getText());
            newUser.setRole((String) roleComboBox.getValue());
            newUser.setAccountStatus("Active"); // Assuming you have a default status
            newUser.setDateCreated(LocalDateTime.now()); // Assuming dateCreated is set at time of creation
            // lastLogin might be null until the user actually logs in

            us.create(newUser); // Assuming create method handles the insertion into the database

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("User created successfully");
            alert.showAndWait();

            clearForm();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }

    }

    public void clearForm() {
        // Clear each text field
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    public void gotologin(ActionEvent event) {
            us.switchView(MainFX.primaryStage, "/front/Login.fxml");
    }

}
