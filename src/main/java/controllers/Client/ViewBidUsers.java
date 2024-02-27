package controllers.Client;

import controllers.Client.AddBidUsers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.Bid;
import services.BidService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewBidUsers implements Initializable {

    @FXML
    private Label highestbid;

    @FXML
    private TableView<Bid> bidTableView;

    @FXML
    private TableColumn<Bid, Integer> idColumn;

    @FXML
    private TableColumn<Bid, Integer> AmountColumn;

    @FXML
    private TableColumn<Bid, Integer> UserColumn;

    private BidService bidService;
    private int auctionId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        setupTableView();

    }

    private void setupTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idbid"));
        AmountColumn.setCellValueFactory(new PropertyValueFactory<>("bidAmount"));
        UserColumn.setCellValueFactory(new PropertyValueFactory<>("userid"));
    }

    public void populateTableView() {
        try {
            // Fetch the highest bid for the auction
            int highestBid = bidService.getHighestBidForAuction(auctionId);

            // Set the highest bid to the label
            highestbid.setText("Highest $"+ highestBid);

            // Populate the TableView with bid data
            ObservableList<Bid> bidData = FXCollections.observableArrayList(bidService.read());
            bidTableView.setItems(bidData);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    @FXML
    private void AddBidUsers() {
        try {
            // Load the Add Bid view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/AddBidClients.fxml"));
            Parent addBidParent = loader.load();

            // Get the controller associated with the Add Bid view
            AddBidUsers addBidUsers = loader.getController();

            // Inject the BidService into the AddBidUsers
            addBidUsers.setBidService(bidService);

            // Set the ViewBidUsers in AddBidUsers
            addBidUsers.setViewBidUsers(this);

            // Create a new stage for the Add Bid view
            Scene scene = new Scene(addBidParent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Bid");

            // Show the Add Bid view
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
        // Initialize BidService here after auctionId is set
        bidService = new BidService(auctionId);
        populateTableView();
    }
}
