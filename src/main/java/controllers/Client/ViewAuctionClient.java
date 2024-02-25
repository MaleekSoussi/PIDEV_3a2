package controllers.Client;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Auction;
import services.AuctionService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewAuctionClient implements Initializable {

    @FXML
    private GridPane itemsContainer;

    private AuctionService auctionService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemsContainer.setHgap(40);
        itemsContainer.setVgap(40);
        auctionService = new AuctionService();

        // Fetch data from the database
        List<Auction> auctions = null;
        try {
            auctions = auctionService.read();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle database exception appropriately
        }

        // Generate items based on the fetched data
        if (auctions != null) {
            int columnIndex = 0;
            int rowIndex = 0;
            for (Auction auction : auctions) {
                try {
                    // Load the item FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Items.fxml"));
                    VBox item = loader.load();

                    // Set the column index
                    GridPane.setColumnIndex(item, columnIndex);
                    GridPane.setRowIndex(item, rowIndex);

                    // Add the item to the GridPane
                    itemsContainer.getChildren().add(item);

                    // Update column and row indices
                    columnIndex++;
                    if (columnIndex  == 2) {
                        columnIndex = 0;
                        rowIndex++;
                    }

                    // Retrieve the controller and set auction data
                    ItemController itemController = loader.getController();
                    itemController.setAuctionData(auction);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }
    }
}
