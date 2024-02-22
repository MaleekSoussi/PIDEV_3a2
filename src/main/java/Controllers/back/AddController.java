package Controllers.back;

import Models.Users;
import Services.UserService;
import Test.MainFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AddController {

    @FXML
    private TextField email;

    @FXML
    private TextField firstname;


    @FXML
    private TextField lastname;

    @FXML
    private TextField password;

    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private ComboBox<String> statusComboBox;
    private UserService us = new UserService();

    @FXML
    public void initialize() {
        // Initialize the role combo box
        roleComboBox.getItems().addAll("Artist", "Amateur");
        // Initialize the status combo box
        statusComboBox.getItems().addAll("Active", "Disabled");
        // Select the first item by default if needed
        roleComboBox.getSelectionModel().selectFirst();
        statusComboBox.getSelectionModel().selectFirst();
    }


    @FXML
    void add(ActionEvent event) {
        String firstName = firstname.getText();
        String lastName = lastname.getText();
        String email = this.email.getText();
        String selectedRole = (String) roleComboBox.getValue();
        String selectedStatus = (String) statusComboBox.getValue();
        String userPassword = password.getText();

        // Input validation (you can expand this based on your requirements)
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || userPassword.length() < 3) {
             us.showAlert(Alert.AlertType.ERROR, "Form Error", "Please complete all fields.");
            return;
        }

        // Email, Role, Status validation here (similar to your signup logic)

        // Create the user object
        Users newUser = new Users();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmailAddress(email);
        newUser.setRole(selectedRole);
        newUser.setAccountStatus(selectedStatus);
        newUser.setPassword(userPassword); // Make sure to hash the password if necessary
        // other fields like dateCreated can be set here as well

        // Attempt to create the user in the database
        try {
            us.create(newUser); // Assuming the UserService create method adds the user to the database
             us.showAlert(Alert.AlertType.INFORMATION, "User Added", "The new user has been added successfully.");

        } catch (SQLException e) {
             us.showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add the new user: " + e.getMessage());
        }
    }

    @FXML
    void backbutton(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/back/Dashboard.fxml");
    }

}
