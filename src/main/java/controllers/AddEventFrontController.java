package controllers;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.Event;
import services.EventService;

import java.sql.SQLException;
import java.time.LocalDate;

public class AddEventFrontController {

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
    private Button createEventButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private VBox eventsVBox;

    private final EventService eventService = new EventService();

    // This method is already provided by you.
    @FXML
    private void createEvent(ActionEvent event) {
        try {
            Event newEvent = new Event(
                    eventNameTextField.getText(),
                    LocalDate.parse(eventDateTextField.getText()),
                    Integer.parseInt(eventDurationTextField.getText()),
                    eventTypeTextField.getText(),
                    Double.parseDouble(entryFeeTextField.getText()),
                    Integer.parseInt(capacityTextField.getText()),
                    null // Placeholder for the image path
            );

            eventService.create(newEvent);
            showAlert("Success", "Event created successfully!");
            clearTextFields();
        } catch (SQLException | NumberFormatException e) {
            showAlert("Error", e.getMessage());
        }
    }

    // Placeholder for updateEvent logic.
    @FXML
    private void updateEvent(ActionEvent event) {
        try {
            Event updatedEvent = new Event(
                    eventNameTextField.getText(),
                    LocalDate.parse(eventDateTextField.getText()),
                    Integer.parseInt(eventDurationTextField.getText()),
                    eventTypeTextField.getText(),
                    Double.parseDouble(entryFeeTextField.getText()),
                    Integer.parseInt(capacityTextField.getText()),
                    null // Placeholder for the image path
            );

            // Assuming you have an update method in your service.
            eventService.update(updatedEvent);
            showAlert("Success", "Event updated successfully!");
            clearTextFields();
        } catch (SQLException | NumberFormatException e) {
            showAlert("Error", e.getMessage());
        }
    }

    // Placeholder for deleteEvent logic.
    @FXML
    private void deleteEvent(ActionEvent event) {
        try {
            // Assuming that you can retrieve the event ID from the event name,
            // and that your EventService has a method to get an event ID by name.
            int eventId = eventService.getEventIdByName(eventNameTextField.getText());

            eventService.delete(eventId);
            showAlert("Success", "Event deleted successfully!");
            clearTextFields();
        } catch (SQLException e) {
            showAlert("Error", e.getMessage());
        }
    }

    public void initialize() throws SQLException {
        // Assume getEvents() retrieves a list of events
        List<Event> events = eventService.getAllEvents();
        for (Event event : events) {
            eventsVBox.getChildren().add(createEventCard(event));
        }
    }

    private HBox createEventCard(Event event) throws SQLException {
        HBox eventCard = new HBox();
        eventCard.getStyleClass().add("event-card");
        eventCard.setSpacing(10);

        VBox detailsBox = new VBox();
        detailsBox.setSpacing(5);
        Label titleLabel = new Label(event.getNameE()); // Assuming getName() is the correct method
        titleLabel.getStyleClass().add("event-title");
        Label detailsLabel = new Label(eventService.getDetails(event.getNameE())); // Assuming getDetails() is the correct method
        detailsLabel.getStyleClass().add("event-details");

        detailsBox.getChildren().addAll(titleLabel, detailsLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button participateButton = new Button("Participate");
        participateButton.setOnAction(e -> handleParticipate(event));

        eventCard.getChildren().addAll(detailsBox, spacer, participateButton);

        return eventCard;
    }


    private void handleParticipate(Event event) {
        // Your participation logic here
    }

    // Placeholder for insertEventImage logic.
    @FXML
    public void insertEventImage(ActionEvent event) {
        // This should handle file choosing and image processing.
        showAlert("Info", "Image insertion feature is not implemented yet.");
    }

    // Utility method for showing alerts.
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Utility method for clearing all text fields.
    private void clearTextFields() {
        eventNameTextField.clear();
        eventDateTextField.clear();
        eventDurationTextField.clear();
        eventTypeTextField.clear();
        entryFeeTextField.clear();
        capacityTextField.clear();
    }
}
