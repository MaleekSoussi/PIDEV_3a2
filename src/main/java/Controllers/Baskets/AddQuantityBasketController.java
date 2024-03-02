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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

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
        // Host your icon on a web server and use the public URL here


        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html>");
        htmlContent.append("<head>");
        htmlContent.append("<title>Shared Basket</title>");
        htmlContent.append("</head>");
        htmlContent.append("<body style=\"font-family: 'Arial', sans-serif; color: #333; background-color: #f4f4f4; padding: 20px; margin: 0; box-sizing: border-box;\">");

        // Inline the styles for the h2 element
        htmlContent.append("<h2 style=\"color: #173e80; text-align: center; padding: 20px; margin-bottom: 20px;\">");
        htmlContent.append("<img src=\"").append("\" alt=\"Basket\" style=\"vertical-align: middle; width: 24px; height: 24px; margin-right: 10px;\">"); // Icon next to the text
        htmlContent.append("Your basket</h2>");

        // Inline the styles for the table and its children
        htmlContent.append("<table style=\"width: 80%; margin: 0 auto; border-collapse: collapse;\">");
        htmlContent.append("<tr><th style=\"background-color: #173e80; color: #fff; padding: 15px; text-align: left; border-radius: 10px 10px 0 0;\">Quantity</th><th style=\"background-color: #173e80; color: #fff; padding: 15px; text-align: left; border-radius: 10px 10px 0 0;\">Total Price</th></tr>");
        htmlContent.append("<tr><td style=\"padding: 15px; border: 1px solid #ccc;\">").append(quantity).append("</td><td style=\"padding: 15px; border: 1px solid #ccc;\">").append(totalPrice).append("</td></tr>");
        htmlContent.append("</table>");

        htmlContent.append("</body>");
        htmlContent.append("</html>");

        return htmlContent.toString();
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
    /*public void applyLoyaltyDiscount(int userId, int basketId) {
        Basket basket = basketService.findById(basketId); // Supposons que cette méthode récupère le panier par son ID
        float discountRate = calculateDiscountRate(userId);

        float newTotalPrice = basket.getTotalPrice() * (1 - discountRate);
        basket.setTotalPrice(newTotalPrice);
        basketService.update(basket); // Supposons que cette méthode met à jour le panier avec le nouveau prix total

        System.out.println("Une réduction de " + (discountRate * 100) + "% a été appliquée. Nouveau total: " + newTotalPrice);
    }

    /**
     * Calcule le taux de réduction basé sur l'historique des commandes de l'utilisateur.
     * @param userId Identifiant de l'utilisateur
     * @return Taux de réduction


    private float calculateDiscountRate(int userId) {
        int numberOfOrders = OrderService.getNumberOfOrdersByUser(userId); // Implémentez cette méthode dans OrderService
        if (numberOfOrders >= 10) {
            return 0.15f; // 15% de réduction après 10 commandes
        } else if (numberOfOrders >= 5) {
            return 0.10f; // 10% de réduction après 5 commandes
        }
        return 0.0f; // Pas de réduction pour moins de 5 commandes
    }*/
}

