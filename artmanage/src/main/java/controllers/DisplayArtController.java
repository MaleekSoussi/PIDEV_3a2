package controllers;

import entities.art;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import services.ArtServices;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DisplayArtController {
    private final ArtServices ps = new ArtServices();
    private ObservableList<art> ol;

    int id = 0;

    @FXML
    private TableView<art> ArtTableView;

    @FXML
    private TableColumn<art, String> titlec;

    @FXML
    private TableColumn<art, String> materialsc;

    @FXML
    private TableColumn<art, Double> widthc;

    @FXML
    private TableColumn<art, Double> heightc;

    @FXML
    private TableColumn<art, String> cityc;

    @FXML
    private TableColumn<art, String> typec;

    @FXML
    private TableColumn<art, String> descriptionc;

    @FXML
    private TableColumn<art, Float> priceT;
    @FXML
    private TextField titleu;
    @FXML
    private TextField matiralsu;
    @FXML
    private TextField widthu;

    @FXML
    private TextField heightu;


    @FXML
    private TextField typeu;
    @FXML
    private TextField cityu;

    @FXML
    private TextField descriptionu;
    @FXML
    private TextField prices;


    @FXML
    private Button deleteC;


    @FXML
    private Button updateC;

    @FXML
    void initialize() {
        try {
            List<art> artList = ps.display();
            ol = FXCollections.observableList(artList);
            ArtTableView.setItems(ol);
            titlec.setCellValueFactory(new PropertyValueFactory<>("title"));
            materialsc.setCellValueFactory(new PropertyValueFactory<>("materials"));
            widthc.setCellValueFactory(new PropertyValueFactory<>("width"));
            heightc.setCellValueFactory(new PropertyValueFactory<>("height"));
            typec.setCellValueFactory(new PropertyValueFactory<>("type"));
            cityc.setCellValueFactory(new PropertyValueFactory<>("city"));
            descriptionc.setCellValueFactory(new PropertyValueFactory<>("description"));
            priceT.setCellValueFactory(new PropertyValueFactory<>("price"));


        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    void delete(ActionEvent event) {
         art selectedart= ArtTableView.getSelectionModel().getSelectedItem();
        if (selectedart != null)
        {
            try {
                int id_art = selectedart.getId_art();
                ps.delete(id_art);
                ol.remove(selectedart);
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


    @FXML
    void rereturn(ActionEvent event) {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/addart.fxml"));
            titleu.getScene().setRoot(root);
        }catch(IOException e)
        {
            System.out.println("error" +e.getMessage());
        }

    }

    @FXML
    void update(ActionEvent event) {
        // Récupérer l'objet Conseil sélectionné dans le TableView
        art art = ArtTableView.getSelectionModel().getSelectedItem();

        // Vérifier si un élément a été effectivement sélectionné
        if (art != null) {
            // Mettre à jour les propriétés du Conseil avec les valeurs des champs de texte
            art.setTitle(titleu.getText());
            art.setMaterials(matiralsu.getText());
            art.setHeight(Double.parseDouble(heightu.getText()));
            art.setWidth(Double.parseDouble(widthu.getText()));
            art.setType(typeu.getText());
            art.setCity(cityu.getText());
            art.setDescription(descriptionu.getText());
            art.setWidth(Float.parseFloat(prices.getText()));

            // Appeler la méthode de mise à jour dans votre service ou gestionnaire de données
            try {
                ps.modify(art,id); // Assuming you have an update method in your service
                // Rafraîchir la TableView après la modification
                refreshTableView();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Gérer l'erreur selon vos besoins
            }

            // Réinitialiser les champs de texte et activer le bouton d'ajout
            resetFields();
            //btn_ajouter.setDisable(false);
        }}
        // Méthode pour rafraîchir la TableView après la modification
        private void refreshTableView() {
            try {
                ObservableList<art> observableList = FXCollections.observableList(ps.display());
                ArtTableView.setItems(observableList);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Gérer l'erreur selon vos besoins
            }
        }

        // Méthode pour réinitialiser les champs de texte
        private void resetFields() {
            titleu.clear();
            matiralsu.clear();
            heightu.clear();
            widthu.clear();
            typeu.clear();
            cityu.clear();
            descriptionu.clear();
            prices.clear();
        }

    @FXML
    void click(MouseEvent event) {
            if (event.getButton()== MouseButton.PRIMARY) {
            // Récupérer l'objet Panier sélectionné dans le TableView
            art art = ArtTableView.getSelectionModel().getSelectedItem();
            // Vérifi  er si un élément a été effectivement sélectionné
            if (art != null) {
                // Récupérer les données du Panier et les afficher dans les champs de texte appropriés
                id = art.getId_art();
                titleu.setText(art.getTitle());
                matiralsu.setText(art.getMaterials());
                heightu.setText(String.valueOf(art.getHeight()));
                widthu.setText(String.valueOf(art.getWidth()));
                typeu.setText(art.getType());
                cityu.setText(art.getCity());
                descriptionu.setText(art.getDescription());
                prices.setText(String.valueOf(art.getPrice()));

            }
        }
    }

}