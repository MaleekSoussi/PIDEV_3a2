package controllers.Client;

import controllers.ChatClient.ChatClientController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Bid;
import models.Auction;
import services.AuctionService;
import services.BidService;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewBidUsers implements Initializable {
    @FXML
    private ImageView ImageView;
    @FXML
    private TextArea auctiondescription;
    @FXML
    private Label highestbid;
    @FXML
    private Button chatbutton;
    @FXML
    private Button usersbutton;
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

        try {
            AuctionService auctionService = new AuctionService();
            Auction auction = auctionService.getAuctionById(auctionId);
            if (auction != null) {
                String imagePath = auction.getImgpath();
                Image image = new Image(imagePath);
                ImageView.setImage(image);

                // Set the auction description
                auctiondescription.setText(auction.getDescription());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }


    @FXML
    public void Userschat(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Userschat.fxml"));
            Parent chatWindow = loader.load();

            ChatClientController chatController = loader.getController();
            chatController.setAuctionId(this.auctionId); // Pass the auctionId to the chat controller

            Stage stage = new Stage();
            stage.setScene(new Scene(chatWindow));
            stage.setTitle("Auction Chat: " + this.auctionId);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

}
