package controllers.testing;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Auction;

public class ItemController {


    @FXML
    private Label Auctionname;
    @FXML
    private TextField price;
    @FXML
    private TextField bitcoinprice;

    private Auction auction;

    public void setAuction(Auction auction) {
        this.auction = auction;

        // Set the data of the FXML item based on the provided Auction object
        // Assuming the image path is stored in auctionname
        Auctionname.setText(auction.getAuctionname());
        price.setText(String.valueOf(auction.getPrice()));
        bitcoinprice.setText(String.valueOf(auction.getBitcoin()));

      /*  // Load and set the image (replace with your actual image loading logic)
        Auctionimage.setImage(new Image("path/to/your/images/" + auction.getAuctionname()));
    */}

    @FXML
    public void click() {
    }
}
