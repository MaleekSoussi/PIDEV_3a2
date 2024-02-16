package controllers.Auction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import models.Auction;
import services.AuctionService;
import services.PersonService;

import java.sql.SQLException;

public class AddAuctionController {
    private final AuctionService auctionService = new AuctionService();
    @FXML
    private TextField AuctionNametf;

    @FXML
    private TextField price;

    @FXML
    private Label errorLabel;


    @FXML
    private StackPane alertPopup;


    @FXML
    public void addAuction(ActionEvent event) {
        String auctionName = AuctionNametf.getText().trim();
        String priceStr = price.getText().trim();

        // Clear error label for fresh feedback
        errorLabel.setText("");

        // Validate input
        int priceInt;
        try {
            priceInt = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid price format. Please enter a number.");
            return;
        }

        if (auctionName.isEmpty()) {
            errorLabel.setText("Auction name cannot be empty.");
            return;
        }

        // Check if auctionService is initialized
        if (auctionService == null) {
            errorLabel.setText("Auction service not available. Please inject or initialize it.");
            return;
        }

        try {
            // Use the injected auctionService
            auctionService.create(new Auction(priceInt, auctionName));

            // Handle successful addition (e.g., clear fields, navigate)
            AuctionNametf.clear(); // Clear auction name field (optional)
            price.clear(); // Clear price field (optional)
            showAlertPopup(); // Show success popup (avoid duplicate attempts)
        } catch (SQLException e) {
            errorLabel.setText("Error adding auction: " + e.getMessage());
        }
    }

    // Add a new method to show the alert popup
    private void showAlertPopup() {
        alertPopup.setVisible(true); // Make the popup visible
        alertPopup.requestFocus(); // Focus on the popup to capture user input
    }

    // Add a method to dismiss the alert popup
    @FXML
    public void dismissAlert() {
        alertPopup.setVisible(false); // Hide the popup
        AuctionNametf.clear(); // Clear the auction name field (optional)
        price.clear(); // Clear the price field (optional)
    }

    }



