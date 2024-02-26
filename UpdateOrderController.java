package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import entities.Order;
import services.OrderService;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateOrderController {

    private final OrderService cs = new OrderService();
    private Order order; // Field to store the order to update

    @FXML
    private Button updateButton;
    @FXML
    private TextField dateCu;

    @FXML
    private TextField idOu;

    @FXML
    private TextField totalPu;


    public void setOrder(Order order) {
        if (order == null) {
            System.out.println("The selected order is null.");
            return;
        }
        if (idOu == null) {
            System.out.println("idOu TextField is null. Check FXML injection.");
            return;
        }

        this.order = order;
        idOu.setText(String.valueOf(order.getIdO())); // Set Order ID
        totalPu.setText(String.valueOf(order.getTotalP())); // Set Total Price with two decimal places
        dateCu.setText(order.getDateC()); // Set Date
    }


    @FXML
    void updateButton(ActionEvent event) {
        System.out.println(order.getIdO());
        try {
            int idB = order.getIdB(); // Get idB from the existing order object
            int idO = order.getIdO(); // Get idO from the existing order object
            float totalP = Float.parseFloat(totalPu.getText()); // Parse the total price from the text field
            String dateC = dateCu.getText(); // Get the date from the text field
            String status = order.getStatus(); // Get the status from the existing order object

            // Update the order using the service
            Order updatedOrder = new Order(idO, totalP, dateC, status, idB);
            cs.update(updatedOrder, idO);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Order updated successfully");
            alert.showAndWait();

            resetFields();

        } catch (SQLException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a valid numeric value for total price.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void resetFields() {
        totalPu.clear();
        dateCu.clear();
    }
    @FXML
    void returnButton(ActionEvent event) {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowOrder.fxml"));
            totalPu.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }

    }
}
