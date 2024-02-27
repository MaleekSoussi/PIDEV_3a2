package controllers;

import entities.art;
import entities.category;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ArtServices;

import javafx.scene.layout.AnchorPane;

import javafx.scene.input.MouseEvent;
import services.CategoryServices;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class DisplayArtController  {
    private final ArtServices ps = new ArtServices();
    private ObservableList<art> ol;
    private final CategoryServices categoryServices = new CategoryServices();




    int id = 0;
    @FXML
    private TextField searchforTitle;
    @FXML
    private TableView<art> ArtTableView;
    @FXML
    private TextField pathart;

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
    private ComboBox<category> categoriChoix;
    @FXML
    private TableColumn<art, String> path;
    @FXML
    private TextField paths;

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
    private TableColumn<art, String> categoryT;
    @FXML
    private Button addIMAGES;

    @FXML
    private Button Sort;

    @FXML
    private ImageView addImages;

    @FXML
    private Pane backImages;
    @FXML
    private ImageView imageARTS;



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
            categoryT.setCellValueFactory(cellData -> {
                int categoryId = cellData.getValue().getId_category();
                String categoryName = ""; // Default value
                try {
                    categoryName = categoryServices.getCategoryName(categoryId);
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Error fetching category name: " + e.getMessage());
                    alert.showAndWait();
                }
                   // Set the selected category in the combo box
                return new SimpleStringProperty(categoryName);
            });
            path.setCellValueFactory(new PropertyValueFactory<>("path_image"));

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        List<category> allCategories = null ;
        try {
            allCategories = categoryServices.displayC();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        categoriChoix.setItems(FXCollections.observableList(allCategories));

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
            art.setId_category((categoriChoix.getValue()).getId_category());
            String newImagePath = paths.getText();
            art.setPath_image(newImagePath);

            //System.out.println("New Image Path: " + newImagePath);
            // Appeler la méthode de mise à jour dans votre service ou gestionnaire de données
            try {
                ps.modify(art,id); // Assuming you have an update method in your service
                // Rafraîchir la TableView après la modification
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Art Updated successfully ");
                alert.showAndWait();
                refreshTableView();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                // Gérer l'erreur selon vos besoins
            }

            // Réinitialiser les champs de texte et activer le bouton d'ajout
            resetFields();
            initialize();
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
            categoriChoix.getItems().clear();
            paths.clear();
            imageARTS.setImage(null);




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
                int categoryId = art.getId_category();
                // Find the category object that corresponds to the categoryId
                category selectedCategory = null; // Initialize to null
                for (category cat : categoriChoix.getItems()) {
                    if (cat.getId_category() == categoryId) {
                        selectedCategory = cat;
                        break;
                    }
                }
                // Set the selected category in the combo box
                categoriChoix.setValue(selectedCategory);
                paths.setText(art.getPath_image());



// Load the image from the specified path
                String imagePath = art.getPath_image();
                File imageFile = new File(imagePath);
                Image image = new Image(imageFile.toURI().toString());

// Set the image to the ImageView
                imageARTS.setImage(image);
            }
        }
    }
    @FXML
    void go_cat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/categorieM.fxml"));
            cityu.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }


    @FXML
    void searchForArt(KeyEvent event) {
        String searchQuery = searchforTitle.getText(); // Replace searchTextField with the actual name of your TextField

        // Call the searchProducts method in your ConseilService
        List<art> matchingConseils = ps.searchArt(searchQuery);

        // Update the UI with the matching Conseils
        ol.clear(); // Clear the current data
        ol.addAll(matchingConseils); // Add the matching Conseils to the observable list
        ArtTableView.setItems(ol); // Set the items in the TableView
    }


    @FXML
    void UpdateImages(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")); // Add more supported image formats if needed
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            paths.setText(selectedFile.getPath());
            String destinationFolder = "C:\\xamppp\\htdocs\\ImageArt"; // Change the destination folder path
            File destinationFile = new File(destinationFolder, selectedFile.getName());
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Load the selected image

            Image image = new Image(destinationFile.toURI().toString());
            // Set the image to the ImageView
            imageARTS.setImage(image);
        } else {
            // Handle the case where no file was selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No image file selected");
            alert.showAndWait();
        }
    }
    @FXML
    void Sort(ActionEvent event) {
        // Retrieve the items from the TableView
        ObservableList<art> items = ArtTableView.getItems();

        // Sort the items by title using Comparator
        items.sort(Comparator.comparing(art::getPrice));

        // Update the TableView with the sorted items
        ArtTableView.setItems(items);
    }


}
