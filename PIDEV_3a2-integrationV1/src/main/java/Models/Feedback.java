package Models;

public class Feedback {
    private int feedbackId;
    private String status;
    private String type;
    private String question;
    private String answer;
    private String userSatisfaction;
    private int userId;  // id_U

    // Constructor
    public Feedback(int feedbackId, String status, String type, String question, String answer, String userSatisfaction, int userId) {
        this.feedbackId = feedbackId;
        this.status = status;
        this.type = type;
        this.question = question;
        this.answer = answer;
        this.userSatisfaction = userSatisfaction;
        this.userId = userId;
    }

    // Empty Constructor
    public Feedback() {
    }

    // Getters and Setters
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUserSatisfaction() {
        return userSatisfaction;
    }

    public void setUserSatisfaction(String userSatisfaction) {
        this.userSatisfaction = userSatisfaction;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // toString method
    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", userSatisfaction='" + userSatisfaction + '\'' +
                ", userId=" + userId +
                '}';
    }
}
