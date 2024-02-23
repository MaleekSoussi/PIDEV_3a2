package controllers.testing;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Auction; // Replace with your Auction model class
import services.AuctionService; // Replace with your Auction service class

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListAuctionController {

    private final AuctionService auctionService = new AuctionService();
    private final ObservableList<Auction> observableList;

    @FXML
    private TableColumn<Auction, Integer> auctionIdCol;

    @FXML
    private TableColumn<Auction, String> auctionNameCol;

    @FXML
    private TableColumn<Auction, Integer> auctionPriceCol;

    @FXML
    private TableView<Auction> auctionTableView;

    public ListAuctionController() {
        // Initialize observableList for auctions
        this.observableList = FXCollections.observableArrayList();
    }

    @FXML
    void initialize() {
        try {
            List<Auction> auctionsList = auctionService.read(); // Assuming you have a showAuctions method
            observableList.addAll(auctionsList);
            auctionTableView.setItems(observableList);
            auctionIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            auctionNameCol.setCellValueFactory(new PropertyValueFactory<>("Auctionname"));
            auctionPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    void navigate(ActionEvent event) {

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Auction/AddAuction.fxml"));
                Node sourceNode = (Node) event.getSource();
                Scene scene = sourceNode.getScene();
                scene.setRoot(root);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

