package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import entities.Delivery;
import services.DeliveryService;

import java.io.IOException;
import java.sql.SQLException;

public class AddDeliveryController {

    private final DeliveryService cs = new DeliveryService();

    @FXML
    private Button addButton;

    @FXML
    private TextField datef;

    @FXML
    private ComboBox<String> deliverymodef;

    @FXML
    private TextField idDf;

    @FXML
    private TextField locationf;

    @FXML
    private TextField phonenumf;

    @FXML
    public void initialize() {
        // Initialize the ComboBox with options
        ObservableList<String> deliveryModes = FXCollections.observableArrayList(
                "Express","Standard"); // Add your delivery modes here
        deliverymodef.setItems(deliveryModes);
    }
    @FXML
    void updateButton(ActionEvent event)
    {
            try
            {
                Parent root = FXMLLoader.load(getClass().getResource("/UpdateDelivery.fxml"));
                locationf.getScene().setRoot(root);
            }catch(IOException e)
            {
                System.out.println("error" +e.getMessage());
            }


    }

    @FXML
    void addButton(ActionEvent event) {
        try {
            cs.create(new Delivery(
                    locationf.getText(),
                    datef.getText(),
                    Integer.parseInt(phonenumf.getText()),
                    deliverymodef.getValue()
            ));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Delivery added successfully");
            alert.showAndWait();

            idDf.clear();
            locationf.clear();
            phonenumf.clear();
            deliverymodef.setValue(null);

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid numeric value for ID and phone number.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a location");
            alert.showAndWait();
        }
    }
}
