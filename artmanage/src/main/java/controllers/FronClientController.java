package controllers;

import entities.art;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import services.ArtServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FronClientController {
    @FXML
    private Button LoginArtist;

    @FXML
    private GridPane artGrid;

    @FXML
    private ScrollPane artItems;

    @FXML
    private TextField searchf;
    private ArtServices cs = new ArtServices();

    @FXML
    void aboutusbutton(ActionEvent event) {

    }

    @FXML
    void artworksbutton(ActionEvent event) {

    }

    @FXML
    void auctionbutton(ActionEvent event) {

    }

    @FXML
    void effect(MouseEvent event) {

    }

    @FXML
    void eventbutton(ActionEvent event) {

    }

    @FXML
    void gotofOb(ActionEvent event) {

    }

    @FXML
    void homebutton(ActionEvent event) {

    }

    @FXML
    void searchbutton(ActionEvent event) {

    }

    @FXML
    void viewcartbutton(ActionEvent event) {

    }

    @FXML
    void initialize() throws SQLException, IOException {
        artGrid.setHgap(-20); // Horizontal gap between items
        artGrid.setVgap(-20); // Vertical gap between items
        artItems.setContent(artGrid);
        artItems.setFitToWidth(true);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(10.0); // Set the desired height
        artGrid.getRowConstraints().add(rowConstraints);
        try {
            List<art> artList = cs.display();
            artListF(artList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void artListF(List<art> artList) {
        artGrid.getChildren().clear();
        int columnCount = 0;
        int rowCount = 0;

        for (art art : artList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ItemsArt.fxml"));
                Node artnode = loader.load();
                ItemsArtController itemController = loader.getController();
                itemController.setData(art); // Pass the art data to ItemsArtController
                artGrid.add(artnode, columnCount, rowCount);
                columnCount++;

                if (columnCount == 4) {
                    columnCount = 0;
                    rowCount++;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @FXML
    void GoFrontArtist(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/addart.fxml"));
            artGrid.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void searchArt(KeyEvent event) {

    }

    }
