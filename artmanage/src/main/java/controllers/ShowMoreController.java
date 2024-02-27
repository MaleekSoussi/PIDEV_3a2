package controllers;

import entities.art;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import services.ArtServices;
import services.CategoryServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ShowMoreController {
    private static int idA;

    private ArtServices cs = new ArtServices();

    @FXML
    private Text CatText;

    @FXML
    private Text CityText;

    @FXML
    private Text MaterialsText;

    @FXML
    private Text descriptionText;

    @FXML
    private Text heightText;

    @FXML
    private Text prixText;

    @FXML
    private Text rarityText;

    @FXML
    private Text titleText;

    @FXML
    private Text widthText;


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

    public void setDataa(art art) {
        CategoryServices categoryServices = new CategoryServices();
        titleText.setText(art.getTitle());
        MaterialsText.setText(art.getMaterials());
        heightText.setText(String.valueOf(art.getHeight()));
        widthText.setText(String.valueOf(art.getHeight())); // Assuming this is correct, it should be art.getWidth() normally
        rarityText.setText(art.getType());
        CityText.setText(art.getCity());
        descriptionText.setText(art.getDescription());
        prixText.setText(String.valueOf(art.getPrice()));
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
        CatText.setText(categoryName);
    }

    @FXML
    void initialize() {

    }

    // Helper method to display an alert
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    }



