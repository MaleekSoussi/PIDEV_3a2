package Controllers.front;

import Models.Users;
import Services.UserService;
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
    private Button signUpButton;

    @FXML
    private Button Loginexist;


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: to remove the header text
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void clicksignup(ActionEvent event) {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String name = emailField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = (String) roleComboBox.getValue();


        if (firstName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "First name is required.");
            return;
        }

        // Last Name validation
        if (lastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Last name is required.");
            return;
        }

        // Email validation
        if (email.isEmpty() || !email.matches("^[\\w.-]+@esprit\\.tn$")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Email must not be empty & end with @esprit.tn");
            return;
        }

        // Password validation
        if (password == null || password.length() < 3) {
            showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password must be at least 3 characters long");
            return;
        }

        // Role selection validation
        if (role == null || role.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Role", "You must select a role");
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

            firstNameField.setText("");
            lastNameField.setText("");
            emailField.setText("");
            passwordField.setText("");
            roleComboBox.getSelectionModel().clearSelection();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }

    }


    @FXML
    public void gotologin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Login.fxml")); // Ensure the path is correct
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

}
