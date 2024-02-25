package controllers;


import entities.art;
import entities.category;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import services.ArtServices;
import services.CategoryServices;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AddartController implements Initializable {
    private final ArtServices artps = new ArtServices();
    private final CategoryServices categoryServices = new CategoryServices();

    @FXML
    private Button home;
    @FXML
    private TextField titlef;
    @FXML
    private TextField materialsf;
    @FXML
    private TextField heightf;

    @FXML
    private TextField widthf;
    @FXML
    private TextField typef;
    @FXML
    private TextField cityf;
    @FXML
    private TextField descriptionf;
    @FXML
    private TextField price;
    @FXML
    private Button back;

    @FXML
    private ComboBox<category> typeCategry;

    @FXML
    void add(ActionEvent event) {

        try {
            category SelectedCategory = typeCategry.getValue();
            artps.add(new art(titlef.getText(), materialsf.getText(), Double.parseDouble(widthf.getText()), Double.parseDouble(heightf.getText()), typef.getText(), cityf.getText(), descriptionf.getText() , Float.parseFloat(price.getText()), SelectedCategory.getId_category()));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Art added successfully");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }


    }

    @FXML
    void display(ActionEvent event) throws IOException {
        try {
        Parent root = FXMLLoader.load(getClass().getResource("/displayArt.fxml"));
        titlef.getScene().setRoot(root);

    } catch (IOException e) {
        System.out.println("error"+e.getMessage());
    }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<category> allCategories = null;
        try {
            allCategories = categoryServices.displayC();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Set categories as items in the ComboBox
        typeCategry.setItems(FXCollections.observableList(allCategories));
    }



    @FXML
    void gotoHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GoBackFront.fxml"));
            materialsf.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }
}
