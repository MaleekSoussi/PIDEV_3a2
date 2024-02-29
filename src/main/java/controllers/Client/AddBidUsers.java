package controllers.Client;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import models.Bid;
import services.BidService;

import java.sql.SQLException;

public class AddBidUsers {

    private ViewBidUsers ViewBidUsers;
    private BidService bidService;

    @FXML
    private StackPane alertPopup;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField amountField;

    // Setter method to inject ViewBidUsers
    public void setViewBidUsers(ViewBidUsers ViewBidUsers) {
        this.ViewBidUsers = ViewBidUsers;
    }

    // Setter method to inject BidService
    public void setBidService(BidService bidService) {
        this.bidService = bidService;
    }

    @FXML
    void addBid(ActionEvent event) {
        String amountStr = amountField.getText().trim();

        // Clear error label for fresh feedback
        errorLabel.setText("");

        // Validate input
        int bidAmount;
        try {
            bidAmount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid amount format. Please enter a number.");
            return;
        }

        try {
            Bid newBid = new Bid(bidAmount);// Create a new Bid object

            // Use the injected bidService
            bidService.create(newBid);

            // Send this bid to the server via BidClient
            if (ViewBidUsers.getBidClient() != null) {
                ViewBidUsers.getBidClient().sendBid(new Gson().toJson(newBid));
            }

            // Update the TableView in ViewBidUsers for all sessions
            ViewBidUsers.populateTableView(newBid);

            // Handle successful addition (e.g., clear fields, navigate)
            clearFields();
            showAlertPopup(); // Show success popup (avoid duplicate attempts)

        } catch (SQLException e) {
            errorLabel.setText("Error adding bid: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage()); // Display message for prompting user to bid higher
        } catch (Exception e) {
            // Handle exceptions
        }
    }


    // Method to show the alert popup
    private void showAlertPopup() {
        alertPopup.setVisible(true); // Make the popup visible
        alertPopup.requestFocus(); // Focus on the popup to capture user input
    }

    // Method to clear all input fields
    private void clearFields() {
        amountField.clear(); // Clear amount field
    }

    // Method to dismiss the alert popup
    @FXML
    public void dismissAlert() {
        alertPopup.setVisible(false); // Hide the popup
        clearFields();
    }
}
