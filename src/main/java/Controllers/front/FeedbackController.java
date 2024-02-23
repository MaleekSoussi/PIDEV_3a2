package Controllers.front;

import Controllers.back.UserItemController;
import Models.Feedback;
import Models.Users;
import Services.FeedbackService;
import Services.UserService;
import Test.MainFX;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FeedbackController{

    private UserService us = new UserService();
    private FeedbackService fs = new FeedbackService();
    @FXML
    private Button goback;

    @FXML
    private ScrollPane itemlist;

    @FXML
    private Button logout;

    @FXML
    private VBox vboxFeedbackList;


    public void initialize() {
        vboxFeedbackList = new VBox(5); // Spacing of 5 between items
        vboxFeedbackList.setFillWidth(true); // This will make the VBox fit to the width of ScrollPane
        itemlist.setContent(vboxFeedbackList);
        itemlist.setFitToWidth(true); // This will make the content fit the width of ScrollPane
        try {
            List<Feedback> feedbacks = fs.getFeedbacksByUserId(UserService.currentlyLoggedInUser.getUserID());
            loadFeedbackItems(feedbacks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadFeedbackItems(List<Feedback> feedbacks) {
        vboxFeedbackList.getChildren().clear(); // Clear any existing items
        for (Feedback feedback : feedbacks) {
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/feedbackitem.fxml"));
            Node feedbackNode = loader.load();
            FeedbackItemController itemController = loader.getController();
            itemController.setFeedback(feedback);
            vboxFeedbackList.getChildren().add(feedbackNode);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @FXML
    void Settings(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/Settings.fxml");
    }



    @FXML
    void goback(ActionEvent event) {
        us.switchView(MainFX.primaryStage, "/front/MainWindow.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        // Logout the current user
        UserService.currentlyLoggedInUser = null;
        // Use switchView to change the scene
        us.switchView(MainFX.primaryStage, "/front/MainWindow.fxml");
    }

    @FXML
    void newfeedback(ActionEvent event){us.switchView(MainFX.primaryStage, "/front/NewFeedback.fxml");}
}
