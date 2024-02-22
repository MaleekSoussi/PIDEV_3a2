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
    private Label lastlogin;

    @FXML
    private Label lastname;

    @FXML
    private Label role;

    @FXML
    private Label status;

    private Users user; // The User object for this item
    private UserService us =new UserService();

    private UserInterface userinterface;
    public void setUserinterface(UserInterface instance) {
        this.userinterface = instance; // recuperation of the dashboard instance from the   itemController.setUserinterface(this); line
        //also this way we can notify the dashboardcontroller with user details to updated and also which selected user
    }


//each user is represented by this useritem controller
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
            if(userinterface != null) {
                userinterface.updateDetails(user); // Pass the user to be modified to DashboardController
                userinterface.setSelectedUserID(user.getUserID()); // sending the selected user to dashboard
            }
        });
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
            if (userinterface != null) {
                userinterface.refreshUserList();
                userinterface.clearForm();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






}
