package controllers.Artist;

import controllers.Auction.AddAuctionController;
import controllers.Auction.UpdateAuctionController;
import controllers.Client.ViewBidUsers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Auction;
import services.ArtistService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewAuctionArtist implements Initializable {

    @FXML
    private TableView<Auction> auctionTableView;

    @FXML
    private TableColumn<Auction, String> nameColumn;

    @FXML
    private TableColumn<Auction, Integer> priceColumn;

    @FXML
    private TableColumn<Auction, Float> bitcoinColumn;

    @FXML
    private TableColumn<Auction, String> timeColumn;

    @FXML
    private TableColumn<Auction, String> dateColumn;

    @FXML
    private TableColumn<Auction, Void> updateButtonColumn;

    @FXML
    private TableColumn<Auction, Void> deleteButtonColumn;

    @FXML
    private TableColumn<Auction, Void> viewBidsButtonColumn; // New column for View Bids button
    @FXML
    private final ArtistService ArtistService = new ArtistService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableView();
        populateTableView();
    }

    private void setupTableView() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("auctionname"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        bitcoinColumn.setCellValueFactory(new PropertyValueFactory<>("bitcoin"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        updateButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setOnAction(event -> {
                    Auction auction = getTableView().getItems().get(getIndex());
                    handleUpdateButton(auction); // Call your update auction method here
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        deleteButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Auction auction = getTableView().getItems().get(getIndex());
                    try {
                        ArtistService.delete(auction.getId());
                        auctionTableView.getItems().remove(auction);
                    } catch (SQLException e) {
                        // Handle the exception appropriately, e.g., show an error message
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        viewBidsButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewBidsButton = new Button("View Bids");

            {
                viewBidsButton.setOnAction(event -> {
                    Auction auction = getTableView().getItems().get(getIndex());
                    handleViewBidsButton(auction.getId()); // Call your view bids method here
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewBidsButton);
                }
            }
        });
    }

    private void populateTableView() {
        try {
            ObservableList<Auction> auctionData = FXCollections.observableArrayList(ArtistService.read());
            auctionTableView.setItems(auctionData);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    @FXML
    private void handleUpdateButton(Auction auction) {
        Auction selectedAuction = auctionTableView.getSelectionModel().getSelectedItem();
        if (selectedAuction != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auction/UpdateAuction.fxml"));
            Parent updateAuctionParent;
            try {
                updateAuctionParent = loader.load();
            } catch (IOException e) {
                e.printStackTrace(); // Handle I/O error appropriately
                return;
            }

            UpdateAuctionController updateAuctionController = loader.getController();
            updateAuctionController.setAuction(selectedAuction);

            Stage updateAuctionStage = new Stage();
            updateAuctionStage.setScene(new Scene(updateAuctionParent));
            updateAuctionStage.setTitle("Update Auction");

            // Add listener for successful update
            updateAuctionController.setOnAuctionUpdatedListener(() -> {
                Platform.runLater(() -> {
                    refreshTableView();
                    // Show success message if desired
                });
            });

            updateAuctionStage.show();
        } else {
            // Show error message or prompt user to select an auction
        }
    }

    @FXML
    private void AddAuctionController() {
        // Load the Add Auction view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Auction/AddAuction.fxml"));
        Parent addAuctionParent;
        try {
            addAuctionParent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Get the controller associated with the Add Auction view
        AddAuctionController addAuctionController = loader.getController();

        // Inject the ViewAuctionController into AddAuctionController
        addAuctionController.setViewAuctionArtis(this);

        // Create a new stage for the Add Auction view
        Stage addAuctionStage = new Stage();
        addAuctionStage.setScene(new Scene(addAuctionParent));
        addAuctionStage.setTitle("Add Auction");

        // Show the Add Auction view
        addAuctionStage.show();
    }

    // Method to refresh the table view after adding an auction
    public void refreshTableView() {
        populateTableView();
    }

    // Method to handle view bids button click
    private void handleViewBidsButton(int auctionId) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/ViewBidClients.fxml"));
        Parent viewBidsParent;
        try {
            viewBidsParent = loader.load();
        } catch (IOException e) {
            e.printStackTrace(); // Handle I/O error appropriately
            return;
        }
        ViewBidUsers bidsController = loader.getController();

        // Pass the auction ID to the bid view controller
        bidsController.setAuctionId(auctionId);



        Stage viewBidsStage = new Stage();
        viewBidsStage.setScene(new Scene(viewBidsParent));
        viewBidsStage.setTitle("View Bids");
        viewBidsStage.show();
    }

}
