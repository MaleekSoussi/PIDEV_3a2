package Controllers.back;

import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import Models.Users;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashboardController implements UserInterface {


    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private TextField status;

    @FXML
    private ScrollPane itemlist; // The ScrollPane in your Dashboard.fxml

    @FXML
    private VBox vboxUserList; // The VBox that will contain your user items

    private UserService us = new UserService();

    // Initialize method that sets up the VBox inside the ScrollPane
    public void initialize() {
        vboxUserList = new VBox(5); // Spacing of 5 between items
        vboxUserList.setFillWidth(true); // This will make the VBox fit to the width of ScrollPane
        itemlist.setContent(vboxUserList);
        itemlist.setFitToWidth(true); // This will make the content fit the width of ScrollPane
        refreshUserList(); // call to refresh function
    }

    public void loadUserItems(List<Users> users) {
        vboxUserList.getChildren().clear(); // Clear the list before loading new items
        for (Users user : users) {
            if (user.getRole().contains("Admin")) {
                continue; // Skip this iteration, don't add the user to the list
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/item.fxml"));
                Node userItemNode = loader.load(); // a node for each list
                UserItemController itemController = loader.getController();
                itemController.setUser(user);
                itemController.setUserinterface(this); // Set the user interface to this (reference to this instance of the dashboard controller)
                //establishing comms with the other controller

                vboxUserList.getChildren().add(userItemNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void refreshUserList() {
        // Implementation for refreshing the user list
        try {
            List<Users> users = us.read(); // Use UserService to read users
            loadUserItems(users);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL Exception
        }
    }


    @Override
    public void clearForm() {
        // Clear each text field
        firstname.clear();
        lastname.clear();
        status.clear();
    }


// to show user details in the fields
    @Override
    public void updateDetails(Users user) {
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
        status.setText(user.getAccountStatus());
    }



//selected user is needed to know which is user id is getting updated
    private int selectedUserID;
    @Override
    public void setSelectedUserID(int userID) {
        this.selectedUserID = userID;
    }

    @FXML
    void updateclicked(ActionEvent event) {
        try {
            Users userToUpdate = us.readUser(selectedUserID);
            userToUpdate.setFirstName(firstname.getText());
            userToUpdate.setLastName(lastname.getText());
            userToUpdate.setAccountStatus(status.getText());
            us.update(userToUpdate);
            refreshUserList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void addclicked(MouseEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/Add.fxml"));
        Parent root = loader.load();

        // Get the current stage using the event source
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Set the new scene to the stage with the loaded root
        stage.setScene(new Scene(root));
        stage.show();

    } catch (IOException e) {
        throw new RuntimeException(e);
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
