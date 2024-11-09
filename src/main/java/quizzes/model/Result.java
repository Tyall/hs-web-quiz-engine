package quizzes.model;

public class Result {

    public static final String FEEDBACK_CORRECT = "Congratulations, you're right!";
    public static final String FEEDBACK_WRONG = "Wrong answer! Please, try again.";

    private boolean success;
    private String feedback;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
