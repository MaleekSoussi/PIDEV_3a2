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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class SettingsController{


    @FXML
    private Button cancelbutton;

    @FXML
    private Button deletebutton;

    @FXML
    private TextField emailfield;

    @FXML
    private TextField firstnamefield;

    @FXML
    private TextField lastnamefield;

    @FXML
    private Button savebutton;


    private Users originalUserData;
    private UserService us = new UserService();

    public void initialize() {
        if (UserService.currentlyLoggedInUser != null) {
            originalUserData = UserService.currentlyLoggedInUser; // Store the original user data
            firstnamefield.setText( UserService.currentlyLoggedInUser.getFirstName());
            lastnamefield.setText(  UserService.currentlyLoggedInUser.getLastName());
            emailfield.setText( UserService.currentlyLoggedInUser.getEmailAddress());
        }

    }


    public void Cancel(ActionEvent actionEvent) {
        if (originalUserData != null) {
            firstnamefield.setText(originalUserData.getFirstName());
            lastnamefield.setText(originalUserData.getLastName());
            emailfield.setText(originalUserData.getEmailAddress());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: to remove the header text
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
   public void Save(ActionEvent event) {
        try {

            if (UserService.currentlyLoggedInUser != null) {
                // Retrieve values from text fields
                String firstName = firstnamefield.getText();
                String lastName = lastnamefield.getText();
                String email = emailfield.getText();

                // Update the currentUser object
                UserService.currentlyLoggedInUser.setFirstName(firstName);
                UserService.currentlyLoggedInUser.setLastName(lastName);
                UserService.currentlyLoggedInUser.setEmailAddress(email);

                // Update the user in the database
                us.update(UserService.currentlyLoggedInUser);

                // Optionally, display a success message
                showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Your information has been updated.");
            }
        } catch (SQLException e) {
            // Handle potential SQLException
           showAlert(Alert.AlertType.ERROR, "Database Error", "Error");
            System.out.println(e.getMessage());
        }
    }


    @FXML
    void Delete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Confirm Account Deletion");
        alert.setContentText("Are you sure you want to delete your account? This action cannot be undone.");

        // Show the confirmation dialog
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (UserService.currentlyLoggedInUser != null) {
                    // Delete the user from the database
                    us.delete(UserService.currentlyLoggedInUser.getUserID());

                    // Log out the user
                    UserService.currentlyLoggedInUser=null;

                    // Load and display the main window
                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    Parent root = FXMLLoader.load(getClass().getResource("/front/MainWindow.fxml"));
                    stage.setScene(new Scene(root));
                    stage.show();
                }
            } catch (SQLException | IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the account: " + e.getMessage());
            }
        }
    }

    @FXML
    void goback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/MainWindow.fxml")); // Ensure the path is correct
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
    void logout(ActionEvent event) {
        // Logout the current user
        UserService.currentlyLoggedInUser=null;

        try {
            // Load the main window view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/MainWindow.fxml"));
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




}

