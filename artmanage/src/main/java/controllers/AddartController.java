package controllers;


import entities.art;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import services.ArtServices;

import java.io.IOException;
import java.sql.SQLException;

public class AddartController {
    private final ArtServices artps = new ArtServices();

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
    void add(ActionEvent event) {
        try {
            artps.add(new art(titlef.getText(), materialsf.getText(), Double.parseDouble(widthf.getText()), Double.parseDouble(heightf.getText()), typef.getText(), cityf.getText(), descriptionf.getText()));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Art added successfully");
            alert.showAndWait();
            titlef.setText("");
            materialsf.setText("");
            widthf.setText("");
            heightf.setText("");
            typef.setText("");
            cityf.setText("");
            descriptionf.setText("");
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

    }}
