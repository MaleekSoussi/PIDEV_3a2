package controllers;

import entities.art;
import entities.category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import services.CategoryServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ManageCategController {
    CategoryServices categoryServices = new CategoryServices();
    private ObservableList<category> ol;
    int idC = 0 ;



    @FXML
    private Button addt;

    @FXML
    private TableColumn<?, ?> dates;

    @FXML
    private TextField datet;

    @FXML
    private Button deelets;

    @FXML
    private Button modifys;

    @FXML
    private TableView<category> showCategory;

    @FXML
    private TableColumn<?, ?> types;

    @FXML
    private TextField names;

    @FXML
    void displayC(ActionEvent event) {
        try {
            List<category> categoryList = categoryServices.displayC();
            ol = FXCollections.observableList(categoryList);
            showCategory.setItems(ol);
            types.setCellValueFactory(new PropertyValueFactory<>("name"));
            dates.setCellValueFactory(new PropertyValueFactory<>("date"));


        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }


    @FXML
    void addC(ActionEvent event) {
        try {
            categoryServices.addC(new category(names.getText(),datet.getText()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success !");
            alert.setContentText("added!!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error !");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);

        }

    }

    @FXML
    void deleteC(ActionEvent event)
    {
        category selectedcategory= showCategory.getSelectionModel().getSelectedItem();
        if (selectedcategory != null)
        {
            try {
                int id_category = selectedcategory.getId_category();
                categoryServices.deleteC(id_category);
                ol.remove(selectedcategory);
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
            ObservableList<category> observableList = FXCollections.observableList(categoryServices.displayC());
            showCategory.setItems(observableList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Gérer l'erreur selon vos besoins
        }
    }

    // Méthode pour réinitialiser les champs de texte
    private void resetFields() {
        names.clear();
        datet.clear();

    }

    @FXML
    void clear(ActionEvent event) {
        resetFields();
    }

    @FXML
    void click(MouseEvent event) {
        if (event.getButton()== MouseButton.PRIMARY) {
            // Récupérer l'objet Panier sélectionné dans le TableView
            category category = showCategory.getSelectionModel().getSelectedItem();
            // Vérifi  er si un élément a été effectivement sélectionné
            if (category != null) {
                // Récupérer les données du Panier et les afficher dans les champs de texte appropriés
                idC = category.getId_category();
                names.setText(category.getName());
                datet.setText(category.getDate());

            }
        }
    }


    @FXML
    void modifyC(ActionEvent event)
    {
        category category = showCategory.getSelectionModel().getSelectedItem();

        // Vérifier si un élément a été effectivement sélectionné
        if (category != null) {
            // Mettre à jour les propriétés du Conseil avec les valeurs des champs de texte
            category.setName(names.getText());
            category.setDate(datet.getText());


            // Appeler la méthode de mise à jour dans votre service ou gestionnaire de données
            try {
                categoryServices.modifyC(category,idC); // Assuming you have an update method in your service
                // Rafraîchir la TableView après la modification
                refreshTableView();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Gérer l'erreur selon vos besoins
            }

            // Réinitialiser les champs de texte et activer le bouton d'ajout
            resetFields();
            addt.setDisable(true);
        }
    }

    @FXML
    void goArt(ActionEvent event) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/addart.fxml"));
            names.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }

}
