package Controllers.back;

import Models.Feedback;
import Models.Users;
import Services.FeedbackService;
import Services.UserService;
import Test.MainFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.time.format.DateTimeFormatter;

public class FeedbackItemController {

    @FXML
    private Label Answer;

    @FXML
    private Label Question;

    @FXML
    private Label Type;

    @FXML
    private Button deletebutton;

    @FXML
    private HBox itemC;

    @FXML
    private Label status;

    @FXML
    private Button updatebutton;


    private Feedback feedback; // The User object for this item
    private FeedbackService fs = new FeedbackService();
    private DashboardFeedbackController dashboardFeedbackController;

    public void setFeedback(Feedback feedback, DashboardFeedbackController dashboardFeedbackController) {
        this.feedback = feedback;
        this.dashboardFeedbackController = dashboardFeedbackController;

        // Populate the labels with data from the feedback
        Answer.setText(feedback.getAnswer());
        Question.setText(feedback.getQuestion());
        Type.setText(feedback.getType());
        status.setText(feedback.getStatus());

        deletebutton.setOnAction(event -> handleDeleteFeedback());
        updatebutton.setOnAction(event -> dashboardFeedbackController.updateclicked(feedback));
    }

    public void handleDeleteFeedback() {
        try {
            fs.delete(feedback.getFeedbackId());
            dashboardFeedbackController.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error (show alert to the user, log error, etc.)
        }
    }


}
