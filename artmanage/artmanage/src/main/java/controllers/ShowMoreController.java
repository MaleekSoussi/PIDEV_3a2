package controllers;

import entities.art;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import services.ArtServices;
import services.CategoryServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ShowMoreController {

    @FXML
    private Label CATEGORYA;

    @FXML
    private Label CITYA;

    @FXML
    private Label DESCRIPTIONA;

    @FXML
    private Label HEIGHTA;

    @FXML
    private Label MATERIALSA;

    @FXML
    private Label PRICEA;

    @FXML
    private Label TITLEA;

    @FXML
    private Label TYPEA;

    @FXML
    private Label WIDTHA;

    @FXML
    private AnchorPane apid;
    public void setData(art art)
    {
        CategoryServices categoryServices = new CategoryServices();

        TITLEA.setText(art.getTitle());
        MATERIALSA.setText(art.getMaterials());
        HEIGHTA.setText(String.valueOf(art.getHeight()));
        WIDTHA.setText(String.valueOf(art.getHeight()));
        TYPEA.setText(art.getType());
        CITYA.setText(art.getCity());
        DESCRIPTIONA.setText(art.getDescription());
        PRICEA.setText(String.valueOf(art.getPrice()));
        int categoryId = art.getId_category();
        String categoryName = ""; // Default value
        try {
            categoryName = categoryServices.getCategoryName(categoryId);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error fetching category name: " + e.getMessage());
            alert.showAndWait();
        }
        // Set the category name in the label
        CATEGORYA.setText(categoryName);


    }


}