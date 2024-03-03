package controllers;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;
import models.Event;
import services.EventService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShowEventsAFController
{
    private EventService es = new EventService();


    @FXML
    private GridPane eventsGrid;

    @FXML
    private ScrollPane eventsItems;

    @FXML
    private TextField searchf;

    @FXML
    private ComboBox<String> sortComboBox;

    private ObservableList<Event> observableEvents;

    @FXML
    private Button searchB;
    private controllers.ShowTicketsFController ShowTicketsFController;



    @FXML
    void gotOfOb(ActionEvent event)
    {

    }

    @FXML
    void initialize() {

        observableEvents = FXCollections.observableArrayList();

        sortComboBox.setItems(FXCollections.observableArrayList("Sort by Date", "Sort by Entry Fee", "Sort by Capacity"));
        sortComboBox.getSelectionModel().selectFirst(); // Optionally set a default sort

        sortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            onSortOrderChanged(new ActionEvent(sortComboBox, null));
        });

        eventsGrid.setHgap(30); // Horizontal gap between items
        eventsGrid.setVgap(0); // Vertical gap between items
        eventsItems.setContent(eventsGrid);
        eventsItems.setFitToWidth(true);

        // Set minimum height for each row
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(-250.0); // Set the desired height
        eventsGrid.getRowConstraints().add(rowConstraints);

        refreshEventsList();

        // Add dynamic search functionality to the search field
        searchf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                refreshEventsList();
            } else {
                performSearch(newValue.trim().toLowerCase());
            }
        });
    }

    @FXML
    void onSortOrderChanged(ActionEvent event) {
        String selectedSortOrder = sortComboBox.getValue();
        if (selectedSortOrder != null) {
            switch (selectedSortOrder) {
                case "Sort by Date":
                    observableEvents.sort(Comparator.comparing(Event::getDateE));
                    break;
                case "Sort by Entry Fee":
                    observableEvents.sort(Comparator.comparingDouble(Event::getEntryFeeE));
                    break;
                case "Sort by Capacity":
                    observableEvents.sort(Comparator.comparingInt(Event::getCapacityE));
                    break;
                default:
                    // Optionally handle the default case
                    break;
            }
            refreshEventsGrid();
        }
    }

    private void refreshEventsGrid() {
        eventListF(observableEvents);
    }

    private void refreshEventsList() {
        try {
            List<Event> eventList = es.readE();
            observableEvents.setAll(eventList);
            refreshEventsGrid();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", e.getMessage());
        }
    }



    private void eventListF(List<Event> eventList) {
        eventsGrid.getChildren().clear(); // Clear previous results
        int column = 0;
        int row = 0;

        for (Event event : eventList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventItemA.fxml"));
                Node eventNode = loader.load();
                EventItemAController itemController = loader.getController();
                itemController.setShowEventsAFController(this);
                itemController.setData(event);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                eventsGrid.add(eventNode, column++, row); // Add the event node to the grid
                GridPane.setMargin(eventNode, new Insets(10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void aboutUsButton(ActionEvent event) {

    }

    @FXML
    void artworksButton(ActionEvent event) {

    }

    @FXML
    void auctionButton(ActionEvent event) {

    }

    @FXML
    void effect(MouseEvent event)
    {

    }

    @FXML
    void courseButton(ActionEvent event) {

    }

    @FXML
    void homeButton(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/FrontOrBack.fxml"));
            eventsItems.getScene().setRoot(root);
        }
        catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }
    }

    @FXML
    void searchButtonHandler(ActionEvent event) {
        String searchInput = searchf.getText().trim().toLowerCase();
        if (searchInput.isEmpty()) {
            refreshEventsList();
        } else {
            performSearch(searchInput);
        }
    }

    private void performSearch(String searchInput) {
        // Perform the search with the input provided
        try {
            List<Event> allEvents = es.readE();
            List<Event> searchResults = allEvents.stream()
                    .filter(e -> e.getNameE().toLowerCase().contains(searchInput) ||
                            e.getTypeE().toLowerCase().contains(searchInput))
                    .collect(Collectors.toList());

            eventListF(searchResults); // Update the GridPane with the search results
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not perform the search: " + e.getMessage());
        }
    }




    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void viewCartButton(ActionEvent event) {

    }

}
