package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import models.Event;
import services.EventService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AddEventController {

    @FXML
    private Label imageContainer;
    @FXML
    public Button createEventButton;
    @FXML
    private ImageView fruitImg;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private TextField eventNameTextField;

    @FXML
    private TextField eventDateTextField;

    @FXML
    private TextField eventDurationTextField;

    @FXML
    private TextField eventTypeTextField;

    @FXML
    private TextField entryFeeTextField;

    @FXML
    private TextField capacityTextField;

    @FXML
    private Button insertImageButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Event> eventsTableView;

    @FXML
    private StackPane stackPane;

    @FXML
    private Button returnButton;

    @FXML
    private VBox chosenFruitCard;
    private ObservableList <Event> ol = FXCollections.observableArrayList();
    private static Event e;

    private final EventService eventService = new EventService();

    EventService es = new EventService();

    @FXML
    private void createEvent(ActionEvent event) throws SQLException {
        try {
            es.create(new Event(eventNameTextField.getText(), LocalDate.parse(eventDateTextField.getText()), Integer.parseInt(eventDurationTextField.getText()), eventTypeTextField.getText(), Double.parseDouble(entryFeeTextField.getText()), Integer.parseInt(capacityTextField.getText()), null));
            refreshTableView();
            showAlert("Success", "Event added successfully ");
            clearTextFields();
        } catch (SQLException e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    void click(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Event selectedEvent = eventsTableView.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                eventNameTextField.setText(selectedEvent.getNameE());
                eventDateTextField.setText(String.valueOf(Date.valueOf(selectedEvent.getDateE())));
                eventDurationTextField.setText(String.valueOf(selectedEvent.getDurationE()));
                eventTypeTextField.setText(selectedEvent.getTypeE());
                entryFeeTextField.setText(String.valueOf(selectedEvent.getEntryFeeE()));
                capacityTextField.setText(String.valueOf(selectedEvent.getCapacityE()));
            }
        }
    }

    @FXML
    void updateEvent(ActionEvent event) {
        try {
            Event selectedEvent = eventsTableView.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                Event eventt = new Event();
                eventt.setIdE(selectedEvent.getIdE());
                eventt.setNameE(selectedEvent.getNameE());
                eventt.setDateE(LocalDate.parse(eventDateTextField.getText()));
                eventt.setDurationE(Integer.parseInt(eventDurationTextField.getText()));
                eventt.setTypeE(eventTypeTextField.getText());
                eventt.setEntryFeeE(Double.parseDouble(entryFeeTextField.getText()));
                eventt.setCapacityE(Integer.parseInt(capacityTextField.getText()));

                es.update(eventt);

                refreshTableView();
                clearTextFields();

                showAlert("Success", "Event updated successfully ");
            }
        } catch (SQLException | NumberFormatException e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    void deleteEvent(ActionEvent event) {
        Event selectedEvent = eventsTableView.getSelectionModel().getSelectedItem();
        e = selectedEvent;
        if (selectedEvent != null) {
            try {
                int idE = selectedEvent.getIdE();
                es.delete(idE);

                // Make sure ol is initialized before removing an item
                if (ol != null) {
                    ol.remove(selectedEvent);
                    refreshTableView();
                    showAlert("Success", "Event deleted successfully ");
                } else {
                    showAlert("Error", "ObservableList is not initialized");
                }
            } catch (SQLException e) {
                showAlert("Error", e.getMessage());
            }
        }
    }


    public void refreshTableView() {
        try {
            ObservableList<Event> observableList = FXCollections.observableList(es.getAllEvents());
            eventsTableView.setItems(observableList);
        } catch (SQLException e) {
            showAlert("Error", e.getMessage());
        }
    }



    // Method to fetch data from the database and update TableView
    private void fetchDataAndUpdateTable() {
        try {
            // Assuming you have a method to get the data from the database
            List<Event> eventsList = fetchDataFromDatabase();

            // Create an ObservableList from the fetched data
            ObservableList<Event> eventsObservableList = FXCollections.observableArrayList(eventsList);

            // Set the eventts in the TableView
            eventsTableView.setItems(eventsObservableList);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }
    @FXML
    private void initialize() {
        // Set cell value factories directly on the existing TableView (injected from FXML)
        eventsTableView.getColumns().forEach(column -> {
            if (column.getText().equals("EVENT NAME")) {
                column.setCellValueFactory(new PropertyValueFactory<>("nameE"));
            } else if (column.getText().equals("EVENT DATE (YYYY-MM-DD)")) {
                column.setCellValueFactory(new PropertyValueFactory<>("dateE"));
            } else if (column.getText().equals("EVENT DURATION (hours)")) {
                column.setCellValueFactory(new PropertyValueFactory<>("durationE"));
            } else if (column.getText().equals("EVENT TYPE")) {
                column.setCellValueFactory(new PropertyValueFactory<>("typeE"));
            } else if (column.getText().equals("EVENT ENTRY FEE")) {
                column.setCellValueFactory(new PropertyValueFactory<>("entryFeeE"));
            } else if (column.getText().equals("EVENT CAPACITY")) {
                column.setCellValueFactory(new PropertyValueFactory<>("capacityE"));
            }
        });

        // Fetch all events from the database and update TableView
        try {
            List<Event> allEvents = eventService.getAllEvents();
            eventsTableView.getItems().addAll(allEvents);
        } catch (SQLException e) {
            // Handle the exception appropriately, e.g., log it or show an error message
            e.printStackTrace();
        }

        // Set placeholder text when the table is empty
        eventsTableView.setPlaceholder(new Label("No events to display."));

        // Listen for selection changes in the TableView
        eventsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                // No selection, disable update and delete buttons
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            } else {
                // Selection present, enable update and delete buttons
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
            }
        });

        // First, check if the returnButton is in the StackPane
        if (!stackPane.getChildren().contains(returnButton)) {
            // If it's not, create the returnButton and set its properties
            returnButton = new Button("Return to All Events");
            returnButton.setOnAction(this::returnButtonHandler);
            returnButton.setVisible(false); // Initially, hide the return button

            // Then, add the returnButton to the StackPane
            stackPane.getChildren().add(returnButton);
        } else {
            // If the returnButton is already in the StackPane, you can update its visibility or other properties here
            returnButton.setVisible(true); // Make sure the return button is visible when needed
        }
    }


    // Method to fetch data from the database
    private List<Event> fetchDataFromDatabase() {
        // Perform your database query here
        // For example, if you are using JDBC:
        List<Event> eventsList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pi", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM event")) {

            while (resultSet.next()) {
                // Create Event objects and add them to the list
                Event event = new Event(
                        resultSet.getString("nameE"),
                        resultSet.getDate("dateE").toLocalDate(),
                        resultSet.getInt("durationE"),
                        resultSet.getString("typeE"),
                        resultSet.getDouble("entryFeeE"),
                        resultSet.getInt("capacityE")
                );
                eventsList.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }

        return eventsList;
    }

    // Helper method to create an Event object from UI components
    private Event createEventObjectFromUI() {
        Event event = new Event();

        // Check if the date is not empty before parsing
        if (!eventDateTextField.getText().isEmpty()) {
            try {
                LocalDate startDate = LocalDate.parse(eventDateTextField.getText());
                event.setDateE(startDate);
            } catch (DateTimeParseException e) {
                showAlert("Date Error", "Error parsing the date. Please enter a valid date format.");
                return null;
            }
        } else {
            showAlert("Date Error", "Please enter a start date.");
            return null;
        }

        // Rest of your code for other fields
        event.setNameE(eventNameTextField.getText());
        event.setDurationE(Integer.parseInt(eventDurationTextField.getText()));
        event.setTypeE(eventTypeTextField.getText());
        event.setEntryFeeE(Double.parseDouble(entryFeeTextField.getText()));
        event.setCapacityE(Integer.parseInt(capacityTextField.getText()));
        // Set other properties similarly

        return event;
    }

    // Helper method to show an alert
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void insertEventImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Event Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Show open file dialog
        Stage stage = (Stage) insertImageButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Remove the default image if it exists
        List<Node> existingChildren = stackPane.getChildren();
        existingChildren.removeIf(node -> node instanceof Label);

        if (selectedFile != null) {
            // Define the directory where you want to save the images
            String saveDirectory = "C:\\Users\\marye\\IdeaProjects\\PI\\src\\main\\resources\\images";

            // Create the File object for the destination
            File destinationFile = new File(saveDirectory + selectedFile.getName());

            // Copy the selected file to the destination
            try {
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Image Insertion Error", "Error copying image to destination.");
                return;
            }

            // Load the selected image and set it in the image container
            Image selectedImage = new Image(destinationFile.toURI().toString());
            ImagePattern imagePattern = new ImagePattern(selectedImage);

            // Create a new label for the selected image and add it to the stackPane
            Label imageLabel = new Label();
            imageLabel.setMinSize(200, 200); // Set the size as needed
            imageLabel.setBackground(new Background(new BackgroundFill(imagePattern, CornerRadii.EMPTY, Insets.EMPTY)));
            stackPane.getChildren().add(imageLabel);

            showAlert("Image Inserted", "Event image inserted successfully!");
        } else {
            showAlert("Image Insertion Cancelled", "No image selected.");
        }
    }



    @FXML
    private void searchButtonHandler(ActionEvent event) {
        String searchInput = searchTextField.getText().trim().toLowerCase();

        // Check if the search input is empty
        if (searchInput.isEmpty()) {
            showAlert("Search Error", "Please enter a search term.");
            return;
        }

        // Perform the search based on any attribute containing the exact same value
        ObservableList<Event> allEvents = eventsTableView.getItems();
        List<Event> searchResults = allEvents.stream()
                .filter(e -> eventContainsInput(e, searchInput))
                .collect(Collectors.toList());

        // Show an alert if no matching events are found
        if (searchResults.isEmpty()) {
            showAlert("Search Results", "No matching events found.");
            return;
        }

        // Update the TableView with the search results
        updateTableView(searchResults);

        // Show the return button
        returnButton.setVisible(true);
    }

    @FXML
    private void returnButtonHandler(ActionEvent event) {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/AddEvent.fxml"));
            eventNameTextField.getScene().setRoot(root);
        } catch (IOException e)
        {
            System.out.println("error" + e.getMessage());
        }

        // Hide the return button again
        returnButton.setVisible(false);
    }

    private boolean eventContainsInput(Event event, String searchInput) {
        // Check if any attribute of the event contains the search input
        return event.getNameE().toLowerCase().contains(searchInput) ||
                String.valueOf(event.getDateE()).contains(searchInput) ||
                String.valueOf(event.getDurationE()).contains(searchInput) ||
                event.getTypeE().toLowerCase().contains(searchInput) ||
                String.valueOf(event.getEntryFeeE()).contains(searchInput) ||
                String.valueOf(event.getCapacityE()).contains(searchInput);
        // Add similar checks for other attributes as needed
    }

    private void updateTableView(List<Event> events) {
        // Clear current eventts and add the new search results
        eventsTableView.getItems().clear();
        eventsTableView.getItems().addAll(events);
    }
    // Helper method to clear all text fields
    private void clearTextFields() {
        eventNameTextField.clear();
        eventDateTextField.clear();
        eventDurationTextField.clear();
        eventTypeTextField.clear();
        entryFeeTextField.clear();
        capacityTextField.clear();
    }



}

