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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class SettingsController{




    @FXML
    private TextField emailfield;

    @FXML
    private TextField firstnamefield;

    @FXML
    private TextField lastnamefield;


    private UserService us = new UserService();

    public void initialize() {
        if (UserService.currentlyLoggedInUser != null) {
            firstnamefield.setText( UserService.currentlyLoggedInUser.getFirstName());
            lastnamefield.setText(  UserService.currentlyLoggedInUser.getLastName());
            emailfield.setText( UserService.currentlyLoggedInUser.getEmailAddress());
        }

    }


    public void Cancel(ActionEvent actionEvent) {
        initialize();
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
                if (us.isEmailUnique(email) || !email.matches("^[\\w.-]+@esprit\\.tn$")) {
                    UserService.currentlyLoggedInUser.setEmailAddress(email);
                    us.update(UserService.currentlyLoggedInUser);
                     us.showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Your information has been updated.");
                }
                else
                {
                     us.showAlert(Alert.AlertType.INFORMATION,"Error"  ,"email not unique or doesnt have @esprit.tn");
                    emailfield.setText( UserService.currentlyLoggedInUser.getEmailAddress());
                }

            }
        } catch (SQLException e) {
            us.showAlert(Alert.AlertType.ERROR, "Database Error", "Error");
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
                    us.delete(UserService.currentlyLoggedInUser.getUserID());
                    UserService.currentlyLoggedInUser=null;
                    us.switchView(MainFX.primaryStage, "/front/MainWindow.fxml");
                }
            } catch (SQLException e) {
                 us.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the account: " + e.getMessage());
            }
        }
    }

    @FXML
    void goback(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/MainWindow.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        UserService.currentlyLoggedInUser = null;
        us.clearRememberedUser();
        us.switchView(MainFX.primaryStage, "/front/MainWindow.fxml");
    }


    @FXML
    void Feedback(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Feedback.fxml");
    }

    @FXML
    void Reset(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/ResetPassword.fxml");
    }
}

