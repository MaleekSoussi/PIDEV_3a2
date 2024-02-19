package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import entities.Delivery;
import services.DeliveryService;
import java.sql.SQLException;

public class UpdateDeliveryController {

    private final DeliveryService cs = new DeliveryService();

    @FXML
    private Button updateButton;

    @FXML
    private TextField datefu;

    @FXML
    private ComboBox<String> deliverymodefu;

    @FXML
    private TextField idDfu;

    @FXML
    private TextField locationfu;

    @FXML
    private TextField phonenumfu;

    private Delivery delivery; // Assuming you have a field to store the delivery to update

    @FXML
    public void initialize() {
        // Initialize the ComboBox with options
        ObservableList<String> deliveryModes = FXCollections.observableArrayList(
                "Express", "Standard"); // Add your delivery modes here
        deliverymodefu.setItems(deliveryModes);
    }

    public void initData(Delivery delivery) {

        idDfu.setText(String.valueOf(delivery.getIdD())); // Assuming you have a method getId() in Delivery class
        locationfu.setText(delivery.getLocation());
        datefu.setText(delivery.getDate());
        phonenumfu.setText(String.valueOf(delivery.getPhone_num())); // Assuming you have a method getPhoneNumber() in Delivery class
        deliverymodefu.setValue(delivery.getDelivery_mode()); // Assuming you have a method getDeliveryMode() in Delivery class
    }

    @FXML
    void updateButton(ActionEvent event) {
        try {
            cs.update(new Delivery(
                    locationfu.getText(),
                    datefu.getText(),
                    Integer.parseInt(phonenumfu.getText()),
                    deliverymodefu.getValue()
            ), delivery.getIdD()); // Assuming you have a method getId() in Delivery class

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Delivery updated successfully");
            alert.showAndWait();

            // Clear fields or handle UI updates as needed

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid numeric value for phone number.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a location");
            alert.showAndWait();
        }
    }
}
