package pl.softyal.webquizengine.quizzes.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {

    public static final String FEEDBACK_CORRECT = "Congratulations, you're right!";
    public static final String FEEDBACK_WRONG = "Wrong answer! Please, try again.";

    private boolean success;
    private String feedback;

}
