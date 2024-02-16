package controllers.Auction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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

    @FXML
    void updateAuction(ActionEvent event) {
        try {
            int id = Integer.parseInt(idTf.getText());
            String newName = AuctionNameTf.getText();
            int newPrice = Integer.parseInt(priceTf.getText());

            auctionService.update(new Auction( id,newPrice,newName));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Auction updated successfully");
            alert.showAndWait();

            // Clear fields or navigate to a different view
        } catch (SQLException e) {
            handleError(e);
        }
    }

    // Method to handle errors and display alert popups
    private void handleError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
