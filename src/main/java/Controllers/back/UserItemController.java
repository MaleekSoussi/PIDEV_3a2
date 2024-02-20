package Controllers.back;

import Models.Users;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class UserItemController {

    @FXML
    private Label datecreated;

    @FXML
    private Button deletebutton;

    @FXML
    private Button updatebutton;

    @FXML
    private Label email;

    @FXML
    private Label firstname;

    @FXML
    private HBox itemC;

    @FXML
    private Label lastlogin;

    @FXML
    private Label lastname;

    @FXML
    private Label role;

    @FXML
    private Label status;

    private Users user; // The User object for this item
    private UserService us =new UserService();

    private UserDetailUpdater detailUpdater;

    public void setDetailUpdater(UserDetailUpdater updater) {
        this.detailUpdater = updater;
    }


    // Method to set the User data for this item
    public void setUser(Users user) {
        this.user = user;

        // Set the labels to the user's data
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
        email.setText(user.getEmailAddress());
        role.setText(user.getRole());
        status.setText(user.getAccountStatus());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        datecreated.setText(user.getDateCreated() != null ? user.getDateCreated().format(formatter) : "empty");
        lastlogin.setText(user.getLastLogin() != null ? user.getLastLogin().format(formatter) : "Never logged in");


        // Add an action listener to the delete button
        deletebutton.setOnAction(event -> deleteUser());
        updatebutton.setOnAction(event -> {
            if(detailUpdater != null) {
                // Pass the user to be modified to DashboardController
                detailUpdater.updateDetails(user);
                // Set the selectedUserID in DashboardController
                ((DashboardController) detailUpdater).setSelectedUserID(user.getUserID());
            }
        });
    }


    private void updateDetails() {
        if(detailUpdater != null) {
            detailUpdater.updateDetails(user);
        }
    }

    private void deleteUser() {
        try {
            // Debugging output to check the correct ID
            System.out.println("Attempting to delete user with ID: " + user.getUserID());

            // Perform the delete operation
            us.delete(user.getUserID());

            // Check the result of the delete operation
            System.out.println("User with ID " + user.getUserID() + " deleted successfully.");

            // If a callback exists, run it to refresh the user list
            if (detailUpdater != null) {
                detailUpdater.refreshUserList();
                detailUpdater.clearForm();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }






}
