package Controllers.Orders;

import Models.Order;
import Services.OrdersAndBaskets.BasketService;
import Services.OrdersAndBaskets.OrderService;
import Services.User.UserService;
import jakarta.mail.MessagingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import Utils.EmailSend;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class AddOrderController {

    private final OrderService orderService = new OrderService();

    private final BasketService bs = new BasketService();

    @FXML
    private DatePicker dateCA;
    @FXML
    private TextField idBa;
    @FXML
    private TextField totalPA;
    @FXML
    private TextField idOA;

    @FXML
    private TextField cardNumberInput;
    @FXML
    private TextField mmInput;
    @FXML
    private TextField yyInput;
    @FXML
    private TextField cvcInput;
    @FXML
    private TextField zipInput;


    @FXML
    public void initialize() {
        // Correctly initialize the latestBasketId
        int latestBasketId = bs.getLatestBasketId(); // Assuming getLatestBasketId() throws SQLException
        idBa.setText(Integer.toString(latestBasketId));
        System.out.println(latestBasketId);
    }


    @FXML
    void addButton(ActionEvent event) {
      int  Userid= UserService.currentlyLoggedInUser.getUserID();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = dateCA.getValue(); // Get the LocalDate directly from DatePicker

            // Check if the date is not null
            if (date == null) {
                showErrorAlert("Please select a valid date.");
                return;
            }

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
                    "Pending",Userid

            );



            // Assuming you have a method to add the order in OrderService
             orderService.create(newOrder);
            System.out.println("Debug: idB in Order object = " + newOrder.getIdB());

            // Send email notification
            String toEmail = "nourzghal5@gmail.com"; // Replace with actual recipient email
            String subject = "Order Confirmation";
            String content = "Your order has been successfully placed. Thank you for your purchase!";
            EmailSend.MailSender(toEmail, subject, content); // Call MailSender to send the email

            showInfoAlert("Order added successfully");

            clearFields();

        } catch (SQLException e) {
            showErrorAlert("Error adding order: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Please ensure all fields are filled correctly.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            showErrorAlert("Failed to send confirmation email.");
        } // No need for an else after catch blocks
    }


    @FXML
    void returnA(ActionEvent event) {
        navigateTo("/Baskets/Basket.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            totalPA.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", null, "Error loading " + fxmlPath + ": " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    void updateButton(ActionEvent event) {
        try {
            int orderId = Integer.parseInt(idOA.getText());
            Order orderToUpdate = orderService.getOrder(orderId);

            if (orderToUpdate == null) {
                throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Orders/UpdateOrder.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("/Orders/ShowOrder.fxml"));
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
        dateCA.setValue(null);
    }

    @FXML
    void paiement(ActionEvent event) {
        // Assuming the Payment.fxml file is in the same directory as the AddOrder.fxml
        navigateTo("/Orders/Payment.fxml");
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

        }
