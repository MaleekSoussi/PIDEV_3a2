package controllers;

import entities.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import services.BasketService;
import services.OrderService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddOrderController {

    private final OrderService orderService = new OrderService();

    private final BasketService bs = new BasketService();

    @FXML
    private TextField dateCA;
    @FXML
    private TextField idBa;
    @FXML
    private TextField totalPA;
    @FXML
    private TextField idOA;


    @FXML
    public void initialize() {
        // Correctly initialize the latestBasketId
        int latestBasketId = bs.getLatestBasketId(); // Assuming getLatestBasketId() throws SQLException
        idBa.setText(Integer.toString(latestBasketId));
        System.out.println(latestBasketId);
    }


    @FXML
    void addButton(ActionEvent event) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String dateText = dateCA.getText().trim();

            // Validate the date format
            if (!dateText.matches("\\d{2}-\\d{2}-\\d{4}")) {
                throw new DateTimeParseException("Invalid date format", dateText, 0);
            }

            LocalDate date = LocalDate.parse(dateText, formatter);
            System.out.println("Debug: TextField content = " + idBa.getText());

            int idB = Integer.parseInt(idBa.getText());

            // Make sure idB exists in the basket table
            if (!bs.exists(idB)) {
                showErrorAlert("Basket ID " + idB + " does not exist.");
                return;
            }

            Order newOrder = new Order(
                    idB,
                    Float.parseFloat(totalPA.getText()),
                    formatter.format(date),
                    "Pending"
            );

            System.out.println("Debug: idB in Order object = " + newOrder.getIdB());

            orderService.create(newOrder);
            showInfoAlert("Order added successfully");
            clearFields();

        } catch (SQLException e) {
            showErrorAlert("Error adding order: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter valid numeric values.");
        } catch (DateTimeParseException e) {
            showErrorAlert("Please enter the date in the format dd-MM-yyyy");
        }

    }

    @FXML
    void updateButton(ActionEvent event) {
        try {
            int orderId = Integer.parseInt(idOA.getText());
            Order orderToUpdate = orderService.getOrder(orderId);

            if (orderToUpdate == null) {
                throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateOrder.fxml"));
            Parent root = loader.load();
            UpdateOrderController updateController = loader.getController();
            updateController.setOrder(orderToUpdate);
            totalPA.getScene().setRoot(root);

        } catch (IOException e) {
            showErrorAlert("Error loading update order view: " + e.getMessage());
        } catch (SQLException e) {
            showErrorAlert("Error retrieving order information: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter a valid numeric value for ID.");
        } catch (IllegalArgumentException e) {
            showErrorAlert(e.getMessage());
        }
    }

    @FXML
    void displayButton(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowOrder.fxml"));
            dateCA.getScene().setRoot(root);
        } catch (IOException e) {
            showErrorAlert("Error loading display order view: " + e.getMessage());
        }
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        totalPA.clear();
        dateCA.clear();
    }



}
