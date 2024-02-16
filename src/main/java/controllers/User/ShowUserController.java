package controllers.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Person;
import services.PersonService;

import java.sql.SQLException;
import java.util.List;

public class ShowUserController {
private final PersonService ps = new PersonService();
  private  ObservableList<Person> observableList;

    @FXML
    private TableColumn<Person, Integer> ageCol;

    @FXML
    private TableColumn<Person, String> firstNameCol;

    @FXML
    private TableColumn<Person, String> lastNameCol;

    @FXML
    private TableView<Person> usersTableView;


    @FXML
    void initialize() {
        try {
            List<Person> usersList = ps.read();
            observableList = FXCollections.observableList(usersList);
            usersTableView.setItems(observableList);
            ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();        }

    }
    @FXML
    void removeUser(ActionEvent event) {
        observableList.remove(0);
    }


}
