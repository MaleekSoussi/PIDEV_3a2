package controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.Person;
import services.PersonService;

import java.io.IOException;
import java.sql.SQLException;

public class AddUserController {

    private final PersonService ps = new PersonService();
    @FXML
    private TextField ageTf;

    @FXML
    private TextField firstNameTf;

    @FXML
    private TextField lastNameTf;

    @FXML
    void addUser(ActionEvent event) {
        try {
            ps.create(new Person(Integer.parseInt(ageTf.getText()), firstNameTf.getText(), lastNameTf.getText()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("User added successfully");
            alert.showAndWait();
            ageTf.setText("");
            firstNameTf.setText("");
            lastNameTf.setText("");
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }


    }


    @FXML
    void navigate(ActionEvent event) {
        try {
          //  FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowUser.fxml"));
          //  Parent root= loader.load();

            Parent root = FXMLLoader.load(getClass().getResource("/User/ShowUser.fxml"));
            lastNameTf.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }

    }


}
