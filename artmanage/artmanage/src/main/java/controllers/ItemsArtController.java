package controllers;

import entities.art;
import entities.category;
import services.CategoryServices;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class ItemsArtController {
    public static int idA;
    private FronClientController fronClientController;


    @FXML
    private Label CITY;


    @FXML
    private Label CATEGORY;

    @FXML
    private Label DESCRIPTION;

    @FXML
    private Label HEIGHT;

    @FXML
    private Label MATERIALS;

    @FXML
    private Label PRICE;

    @FXML
    private Label TITLE;

    @FXML
    private Label TYPE;

    @FXML
    private Label WIDTH;

    public void setData(art art)
    {
        CategoryServices categoryServices = new CategoryServices();

        TITLE.setText(art.getTitle());
        MATERIALS.setText(art.getMaterials());
        HEIGHT.setText(String.valueOf(art.getHeight()));
        WIDTH.setText(String.valueOf(art.getHeight()));
        TYPE.setText(art.getType());
        CITY.setText(art.getCity());
        DESCRIPTION.setText(art.getDescription());
        PRICE.setText(String.valueOf(art.getPrice()));
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
        CATEGORY.setText(categoryName);

    }

    @FXML
    private Button showmore;

    @FXML
    void showmore(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowMore.fxml"));
            CATEGORY.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }

    public void get_idA(art art)
    {
        idA=art.getId_art();
    }

}
