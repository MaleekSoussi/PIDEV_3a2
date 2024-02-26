package controllers;

import entities.Basket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.BasketService;

import java.io.IOException;
import java.sql.SQLException;

public class AddQuantityBasketController {

    private BasketService basketService = new BasketService();

    @FXML
    private Button addquantity3;

    @FXML
    private Button continueButton;

    @FXML
    private Button deletequantity2;

    @FXML
    private Label quantityLabel;

    @FXML
    private TextField totalPriceS;

    @FXML
    void addQuantity(ActionEvent event) {
        // Increment the quantity directly without using the basket ID
        int quantity = safeParseInt(quantityLabel.getText(), 0);
        quantity++;
        quantityLabel.setText(String.valueOf(quantity));
    }

    @FXML
    void deleteQuantity(ActionEvent event) {
        int quantity = safeParseInt(quantityLabel.getText(), 0);
        if (quantity > 1) { // Ensure quantity is greater than 1 before decrementing
            quantity--;
            quantityLabel.setText(String.valueOf(quantity));
        } else {
            showErrorAlert("Minimum quantity reached.");
        }
    }

    @FXML
    void navigate(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AddOrder.fxml"));
        Scene scene = continueButton.getScene();
        scene.setRoot(root);
    }

    @FXML
    public void addBasket(ActionEvent actionEvent) {
        int quantity = safeParseInt(quantityLabel.getText(), 0);
        float totalPrice;
        try {
            totalPrice = Float.parseFloat(totalPriceS.getText().trim());
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid total price format.");
            return;
        }

        Basket newBasket = new Basket();
        newBasket.setQuantity(quantity);
        newBasket.setTotalPrice(totalPrice);

        int basketId = basketService.create(newBasket);
        if (basketId > 0) {
            showInfoAlert("Basket added successfully with ID: " + basketId);
        } else {
            showErrorAlert("Error adding basket to the database.");
        }
    }


    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to safely parse integers
    private int safeParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue; // Return default value if parsing fails
        }
    }
}
