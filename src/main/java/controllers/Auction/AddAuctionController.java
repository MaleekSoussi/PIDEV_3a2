package controllers.Auction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import models.Auction;
import services.AuctionService;

import java.sql.SQLException;
import java.time.LocalDate;

public class AddAuctionController {

    private ViewAuctionController viewAuctionController;

    private final AuctionService auctionService = new AuctionService();

    @FXML
    private TextField AuctionNametf;

    @FXML
    private TextField price;

    @FXML
    private TextField Bitcoin;

    @FXML
    private StackPane alertPopup;

    @FXML
    private Label errorLabel;

    @FXML
    private DatePicker dateAuction;

    @FXML
    private TextField Auctiontime;

    // Setter method to inject ViewAuctionController
    public void setViewAuctionController(ViewAuctionController viewAuctionController) {
        this.viewAuctionController = viewAuctionController;
    }

    @FXML
    public void addAuction(ActionEvent event) {
        String auctionName = AuctionNametf.getText().trim();
        String priceStr = price.getText().trim();
        String bitcoinStr = Bitcoin.getText().trim();
        String auctionTime = Auctiontime.getText().trim();
        LocalDate auctionDate = dateAuction.getValue();

        // Clear error label for fresh feedback
        errorLabel.setText("");

        // Validate input
        int priceInt;
        float bitcoinFloat;

        try {
            priceInt = Integer.parseInt(priceStr);
            bitcoinFloat = Float.parseFloat(bitcoinStr);
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid price or bitcoin format. Please enter numbers.");
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
            auctionService.create(new Auction(priceInt, bitcoinFloat, auctionTime, auctionDate.toString(), auctionName));

            // Handle successful addition (e.g., clear fields, navigate)
            clearFields();
            showAlertPopup(); // Show success popup (avoid duplicate attempts)

            // Refresh the table view after adding an auction
            viewAuctionController.refreshTableView();
        } catch (SQLException e) {
            errorLabel.setText("Error adding auction: " + e.getMessage());
        }
    }

    // Method to show the alert popup
    private void showAlertPopup() {
        alertPopup.setVisible(true); // Make the popup visible
        alertPopup.requestFocus(); // Focus on the popup to capture user input
    }

    // Method to clear all input fields
    private void clearFields() {
        AuctionNametf.clear(); // Clear auction name field
        price.clear(); // Clear price field
        Bitcoin.clear(); // Clear Bitcoin field
        Auctiontime.clear(); // Clear Auction time field
        dateAuction.getEditor().clear(); // Clear Date picker
    }

    // Method to dismiss the alert popup
    @FXML
    public void dismissAlert() {
        alertPopup.setVisible(false); // Hide the popup
        clearFields();
    }
}
