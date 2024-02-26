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

        // Set the question and type labels
        Question.setText(feedback.getQuestion());
        Type.setText(feedback.getType());
        status.setText(feedback.getStatus());

        if (feedback.getAnswer() != null && !feedback.getAnswer().isEmpty()) {

            Answer.setText(feedback.getAnswer());
            updatebutton.setVisible(false);
        } else {

            Answer.setText("No answer yet.");
            updatebutton.setVisible(true);
        }

        // Set the action for the delete button
        deletebutton.setOnAction(event -> handleDeleteFeedback());

        // Set the action for the update button if it's visible
        if (updatebutton.isVisible()) {
            updatebutton.setOnAction(event -> dashboardFeedbackController.updateclicked(feedback));
        }
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
