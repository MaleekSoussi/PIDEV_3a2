package controllers;

import entities.art;
import entities.category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.ArtServices;


import java.sql.SQLException;
import java.util.List;

public class ManageArtistController {
    private final ArtServices artps = new ArtServices();
    private ObservableList<art> oll;
    int id = 0;
    private int mode = 0;


    @FXML
    private Button changeD;

    @FXML
    private Button changeL;
    @FXML
    private TextField idT;

    @FXML
    private TextField materialsA;
    @FXML
    private TextField heightA;
    @FXML
    private TextField widthA;

    @FXML
    private TextField TypeA;
    @FXML
    private TextField cityA;
    @FXML
    private TextField descA;
    @FXML
    private TextField priceV;
    @FXML
    private TableColumn<?, ?> titleV;
    @FXML
    private TableColumn<?, ?> materialsv;
    @FXML
    private TableColumn<?, ?> heightv;

    @FXML
    private TableColumn<?, ?> widtht;
    @FXML
    private TableColumn<?, ?> typet;

    @FXML
    private TableColumn<?, ?> cityt;
    @FXML
    private TableColumn<?, ?> descriptiont;
    @FXML
    private TableColumn<?, ?> priceT;

    @FXML
    private Button show;
    @FXML
    private Button addart;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Button deleteart;

    @FXML
    private ImageView fruitImg;

    @FXML
    private TableView<art> showTbleArtis;

    @FXML
    private HBox update;

    @FXML
    private Button updateA;
    @FXML
    private BorderPane sheet;
    @FXML
    private AnchorPane modechanger;


    @FXML
    void addArt(ActionEvent event) {
        try {
            artps.add(new art(idT.getText(), materialsA.getText(), Double.parseDouble(heightA.getText()), Double.parseDouble(widthA.getText()), TypeA.getText(), cityA.getText(), descA.getText(),Float.parseFloat(priceV.getText())));

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Art added successfully");
            alert.showAndWait();
            idT.setText("");
            materialsA.setText("");
            heightA.setText("");
            widthA.setText("");
            TypeA.setText("");
            cityA.setText("");
            descA.setText("");
            priceV.setText("");
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        refreshTableView();

    }


    @FXML
    void show(ActionEvent event) {
        try {
            List<art> artList =artps.display();
            oll = FXCollections.observableList(artList);
            showTbleArtis.setItems(oll);
            titleV.setCellValueFactory(new PropertyValueFactory<>("title"));
            materialsv.setCellValueFactory(new PropertyValueFactory<>("materials"));
            heightv.setCellValueFactory(new PropertyValueFactory<>("width"));
            widtht.setCellValueFactory(new PropertyValueFactory<>("height"));
            typet.setCellValueFactory(new PropertyValueFactory<>("type"));
            cityt.setCellValueFactory(new PropertyValueFactory<>("city"));
            descriptiont.setCellValueFactory(new PropertyValueFactory<>("description"));
            priceT.setCellValueFactory(new PropertyValueFactory<>("price"));

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void deleteArt(ActionEvent event) {
        art selectedart= showTbleArtis.getSelectionModel().getSelectedItem();
        if (selectedart != null)
        {
            try {
                int id_art = selectedart.getId_art();
                artps.delete(id_art);
                oll.remove(selectedart);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Art deleted successfully ");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            refreshTableView();
            resetFields();
        }
    }
    private void refreshTableView() {
        try {
            // Create an instance of ArtServices
            ArtServices artServices = new ArtServices();

            // Call the display method on the instance
            ObservableList<art> observableList = FXCollections.observableList(artServices.display());

            // Set the items in the TableView
            showTbleArtis.setItems(observableList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur selon vos besoins
        }
    }


    private void resetFields() {
        idT.clear();
        materialsA.clear();
        materialsA.clear();
        heightA.clear();
        widthA.clear();
        TypeA.clear();
        cityA.clear();
        descA.clear();
        priceV.clear();


    }

    @FXML
    void clear(ActionEvent event) {
        resetFields();
    }
    @FXML
    void update(ActionEvent event) {
        // Récupérer l'objet Conseil sélectionné dans le TableView
        art art = showTbleArtis.getSelectionModel().getSelectedItem();

        // Vérifier si un élément a été effectivement sélectionné
        if (art != null) {
            // Mettre à jour les propriétés du Conseil avec les valeurs des champs de texte
            art.setTitle(idT.getText());
            art.setMaterials(materialsA.getText());
            art.setHeight(Double.parseDouble(heightA.getText()));
            art.setWidth(Double.parseDouble(widthA.getText()));
            art.setType(TypeA.getText());
            art.setCity(cityA.getText());
            art.setDescription(descA.getText());
            art.setPrice(Float.parseFloat(priceV.getText()));

            // Appeler la méthode de mise à jour dans votre service ou gestionnaire de données
            try {
                artps.modify(art,id); // Assuming you have an update method in your service
                // Rafraîchir la TableView après la modification
               // refreshTableView();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Gérer l'erreur selon vos besoins
            }

            // Réinitialiser les champs de texte et activer le bouton d'ajout
            resetFields();
            refreshTableView();



        }}

    @FXML
    void clicked(MouseEvent event) {

        if (event.getButton() == MouseButton.PRIMARY) {
            // Récupérer l'objet Panier sélectionné dans le TableView
            art art = showTbleArtis.getSelectionModel().getSelectedItem();
            // Vérifi  er si un élément a été effectivement sélectionné
            if (art != null) {
                // Récupérer les données d'arts et les afficher dans les champs de texte appropriés
                id = art.getId_art();
                idT.setText(art.getTitle());
                materialsA.setText(art.getMaterials());
                heightA.setText(String.valueOf(art.getHeight()));
                widthA.setText(String.valueOf(art.getWidth()));
                TypeA.setText(art.getType());
                cityA.setText(art.getCity());
                descA.setText(art.getDescription());
                priceV.setText(String.valueOf(art.getPrice()));



            }
        }

    }




}

