package Controllers.back;

import Models.Feedback;
import Services.FeedbackService;
import Services.UserService;
import Test.MainFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashboardFeedbackController {
    private UserService us = new UserService();
    private FeedbackService fs = new FeedbackService();

    @FXML
    private TextArea adminanswer;


    @FXML
    private ScrollPane itemlist;

    @FXML
    private VBox vboxFeedbackList;

    static int selectedUserId;

    public void initialize() {
        vboxFeedbackList = new VBox(5); // Spacing of 5 between items
        vboxFeedbackList.setFillWidth(true); // This will make the VBox fit to the width of ScrollPane
        itemlist.setContent(vboxFeedbackList);
        itemlist.setFitToWidth(true); // This will make the content fit the width of ScrollPane
        loadFeedbackItems();
    }


    private void loadFeedbackItems() {
        vboxFeedbackList.getChildren().clear();
        try {
            List<Feedback> feedbacks = fs.getFeedbacksByUserId(selectedUserId);
            for (Feedback feedback : feedbacks) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/back/Feeditem.fxml"));
                Node feedbackNode = loader.load();
                FeedbackItemController itemController = loader.getController();
                itemController.setFeedback(feedback, this);
                vboxFeedbackList.getChildren().add(feedbackNode);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void back(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/back/Dashboard.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        us.clearRememberedUser();
        UserService.currentlyLoggedInUser=null;
        us.switchView(MainFX.primaryStage, "/front/MainWindow.fxml");
    }

    @FXML
    void updateclicked(Feedback feedback) {
        String newAnswer = adminanswer.getText();
        if(newAnswer.isEmpty()) {
            us.showAlert(Alert.AlertType.INFORMATION, "Update Error", "answer field empty'.");
            return;
        }
        try {
            if(feedback != null) {
                feedback.setAnswer(newAnswer); // Set the new answer
                feedback.setStatus("Closed");
                fs.update(feedback); // Update the feedback in the database

                adminanswer.clear();
                initialize();

                us.showAlert(Alert.AlertType.INFORMATION, "Feedback Updated", "The feedback has been updated successfully.");
            } else {
                us.showAlert(Alert.AlertType.INFORMATION, "Update Error", "couldnt load feedback to update");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception
        }



    }



}
