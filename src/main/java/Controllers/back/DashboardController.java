package Controllers.back;

import Services.UserService;
import Test.MainFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import Models.Users;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class DashboardController {


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


    @FXML
    private TextField searchField;
    private UserService us = new UserService();

    private ObservableList<Users> masterList = FXCollections.observableArrayList(); // Master list of all users
    private FilteredList<Users> filteredList; // Filtered list for displaying

    public void initialize() {
        vboxUserList = new VBox(5); // Spacing of 5 between items
        vboxUserList.setFillWidth(true); // This will make the VBox fit to the width of ScrollPane
        itemlist.setContent(vboxUserList);
        itemlist.setFitToWidth(true); // This will make the content fit the width of ScrollPane
        try {
            List<Users> users = us.read();
            masterList = FXCollections.observableArrayList(users); // Convert to ObservableList
            filteredList = new FilteredList<>(masterList, p -> true); // Wrap with FilteredList
            loadUserItems(filteredList); // Now pass the FilteredList
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            masterList.setAll(us.read());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        filteredList = new FilteredList<>(masterList, p -> true);

        loadUserItems(filteredList);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(user -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (user.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (user.getEmailAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches email.
                } else if (user.getAccountStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches account status
                } else if (user.getRole().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches role
                }
                return false;
            });
            loadUserItems(filteredList); // Reload the user items with the filtered list
        });

    }

    public void loadUserItems(FilteredList<Users> users) {
        vboxUserList.getChildren().clear();
        for (Users user : users) {
            if (user.getRole().contains("Admin")) {
                continue;
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/item.fxml"));
                Node userItemNode = loader.load();
                UserItemController itemController = loader.getController();
                itemController.setUser(user, this); // Pass 'this' to the item controller ( passing a dash controller to each node)
                vboxUserList.getChildren().add(userItemNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void clearForm() {
        firstname.clear();
        lastname.clear();
        status.clear();
    }

    public void updateDetails(Users user) { // will show the selected item to display
        selectedUserID = user.getUserID();// getting the specific user saved while also getting info to show in the fields
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
        status.setText(user.getAccountStatus());
    }


//selected user is needed to know which is user id is getting updated
    private int selectedUserID;

    @FXML
    void updateclicked() {
        try {
            Users userToUpdate = us.readUser(selectedUserID);
            userToUpdate.setFirstName(firstname.getText());
            userToUpdate.setLastName(lastname.getText());
            String accountStatus = status.getText();
            if (!"Active".equals(accountStatus) && !"Disabled".equals(accountStatus)) {
                us.showAlert(Alert.AlertType.INFORMATION, "Update Error", "Account status must be 'Active' or 'Disabled'.");
                return;
            }
            userToUpdate.setAccountStatus(status.getText());
            us.update(userToUpdate);
            us.showAlert(Alert.AlertType.INFORMATION, "User updated", "The  user has been updated successfully.");
            clearForm();
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void addclicked(MouseEvent event) {
            us.switchView(MainFX.primaryStage, "/back/Add.fxml");

    }

    @FXML
    void logout(ActionEvent event) {
        us.clearRememberedUser();
        UserService.currentlyLoggedInUser=null;
        us.switchView(MainFX.primaryStage, "/front/MainWindow.fxml");
    }


}
