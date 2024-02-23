package controllers.Auction;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Auction;
import services.AuctionService;

import java.sql.SQLException;

public class UpdateAuctionController {

    private final AuctionService auctionService = new AuctionService();

    @FXML
    private TextField idTf;

    @FXML
    private TextField AuctionNameTf;

    @FXML
    private TextField priceTf;

    private OnAuctionUpdatedListener onAuctionUpdatedListener; // For update notification

    public void setOnAuctionUpdatedListener(OnAuctionUpdatedListener listener) {
        this.onAuctionUpdatedListener = listener;
    }

    private Auction auctionToUpdate;

    public void setAuction(Auction auction) {
        this.auctionToUpdate = auction;
        if (auction != null) {
            idTf.setText(String.valueOf(auction.getId()));
            AuctionNameTf.setText(auction.getAuctionname());
            priceTf.setText(String.valueOf(auction.getPrice()));
            // Populate other fields as needed
        }
    }

    @FXML
    void updateAuction(ActionEvent event) {
        try {
            int id = Integer.parseInt(idTf.getText());
            String newName = AuctionNameTf.getText();
            int newPrice = Integer.parseInt(priceTf.getText());

            auctionToUpdate.setId(id); // Update auction details
            auctionToUpdate.setAuctionname(newName);
            auctionToUpdate.setPrice(newPrice);
            // Update other fields as needed

            auctionService.update(auctionToUpdate);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Auction updated successfully");
            alert.showAndWait();

            if (onAuctionUpdatedListener != null) {
                onAuctionUpdatedListener.onAuctionUpdated(); // Notify listener
            }

            handleClose(event); // Close the dialog
        } catch (SQLException e) {
            handleError(e);
        }
    }

    @FXML
    void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void handleError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public interface OnAuctionUpdatedListener {
        void onAuctionUpdated();
    }
}
