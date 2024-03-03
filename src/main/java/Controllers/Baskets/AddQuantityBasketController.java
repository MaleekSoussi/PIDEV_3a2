package Controllers.Baskets;

import Models.Basket;
import Services.OrdersAndBaskets.BasketService;
import Services.OrdersAndBaskets.OrderService;
import Services.User.UserService;
import Utils.EmailSend;
import jakarta.mail.MessagingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddQuantityBasketController {
    private OrderService OrderService = new OrderService();
    private BasketService basketService = new BasketService();
    public static int id;
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
    void navigate(ActionEvent event) {
        // Navigation logic to move to another scene
    }

    @FXML
    public void addBasket(ActionEvent actionEvent) {
        int  Userid= UserService.currentlyLoggedInUser.getUserID();
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
        newBasket.setUserid(Userid);

        int basketId = basketService.create(newBasket);
        if (basketId > 0) {
            showInfoAlert("Basket added successfully with ID: " + basketId);
        } else {
            showErrorAlert("Error adding basket to the database.");
        }
        id=newBasket.getIdB();
    }
    public void applyDiscount(float discountPercentage) {
        float totalPrice;
        try {
            totalPrice = Float.parseFloat(totalPriceS.getText().trim());
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid total price format.");
            return;
        }

        // Calcul de la réduction
        float discountAmount = totalPrice * (discountPercentage / 100);
        float finalPrice = totalPrice - discountAmount;

        // Mise à jour du prix total après réduction
        totalPriceS.setText(String.format("%.2f", finalPrice));

        showInfoAlert("A discount of " + discountPercentage + "% has been applied. New total price: " + finalPrice);
    }

    @FXML
    void onApplyDiscountClicked(ActionEvent event) {
        // Appliquer une réduction de 20%
        applyDiscount(20.0f);
    }
    @FXML
    void navigateC(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Orders/AddOrder.fxml"));
        Scene scene = continueButton.getScene();
        scene.setRoot(root);
    }

    @FXML
    void ShareBasket(ActionEvent event) {
        // Ask the user for the recipient's email address
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Share Basket");
        dialog.setHeaderText("Enter recipient's email address:");
        dialog.setContentText("Email:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(email -> {
            // Retrieve price and quantity information
            int quantity = safeParseInt(quantityLabel.getText(), 0);
            float totalPrice;
            try {
                totalPrice = Float.parseFloat(totalPriceS.getText().trim());
            } catch (NumberFormatException e) {
                showErrorAlert("Invalid total price format.");
                return;
            }

            // Generate the email content with the shared basket information and interface components
            String subject = "Shared Basket";
            String content = generateEmailContent(quantity, totalPrice);

            try {
                // Send the email with the generated content
                EmailSend.MailSender(email, subject, content);
                showInfoAlert("Basket shared successfully with " + email);
            } catch (MessagingException | UnsupportedEncodingException e) {
                showErrorAlert("Failed to share basket. Please try again later.");
            }
        });
    }

    private String generateEmailContent(int quantity, float totalPrice) {
        try (InputStream inputStream = this.getClass().getResourceAsStream("/CSS/emailStyle.html")) {
            if (inputStream == null) {
                throw new IOException("Email template file not found.");
            }

            String htmlContent = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            // Define the table with dynamic content
            String dynamicTable = "<table style=\"width: 100%; border-collapse: collapse; margin-top: 20px;\">" +
                    "<tr><th>Quantity</th><th>Total Price</th></tr>" +
                    "<tr><td>" + quantity + "</td><td>" + String.format("%.2f", totalPrice) + "</td></tr>" +
                    "</table>";

            // Find the closing tag of the last <span> that is within the <h1>
            htmlContent = htmlContent.replaceFirst("(?i)(<h1 class=\"v-text-align\"[^>]*>(?:<span[^>]*>)*.*?</span></span></span></span></span></span></span></h1>)", "$1" + dynamicTable);

            return htmlContent;
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to generate email content.";
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
    @FXML
    void applyLoyaltyDiscount(ActionEvent event) {
        try {
            // Retrieve the currently logged-in user ID
            int userId = UserService.currentlyLoggedInUser.getUserID();

            // Check the number of orders to determine if a discount should be applied
            int numberOfOrders = OrderService.getNumberOfOrdersByUser(userId);
            if (numberOfOrders >= 5) {
                // Retrieve the latest basket ID for the user
                Basket basket = basketService.getBasketByUserId(userId);
                int idB = basket.getIdB();
                // Apply the loyalty discount to the basket
                basketService.applyLoyaltyDiscount(idB);

                // Retrieve the updated basket from the database
                Basket updatedBasket = basketService.getBasketById(idB);

                // Update the UI with the new total price after discount
                totalPriceS.setText(String.format("%.2f", updatedBasket.getTotalPrice()));

                // Display success message
                showInfoAlert("Loyalty discount applied successfully.");
            } else {
                // Inform the user that they do not have enough orders to qualify for a discount
                showErrorAlert("You need to have at least 5 orders to qualify for a loyalty discount.");
            }
        } catch (Exception e) {
            // Display error message if something goes wrong
            showErrorAlert("Failed to apply loyalty discount: " + e.getMessage());
        }
    }


    public void homebutton(ActionEvent actionEvent) {
    }
}