package controllers.Auction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import services.AuctionService;

import java.sql.SQLException;

public class DeleteAuctionController {

    private final AuctionService auctionService = new AuctionService();

    @FXML
    private TextField idTf;

    @FXML
    void deleteAuction(ActionEvent event) {
        try {
            int id = Integer.parseInt(idTf.getText());

            auctionService.delete(id);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Auction deleted successfully");
            alert.showAndWait();

            // Clear field or navigate to a different view
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
