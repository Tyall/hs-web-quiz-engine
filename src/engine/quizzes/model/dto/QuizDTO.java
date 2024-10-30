package engine.quizzes.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import engine.users.model.QuizUserAdapter;
import engine.users.model.entity.QuizUser;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class QuizDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @NotNull
    @Size(min = 2)
    private List<String> options;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> answer;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private QuizUser creator;

    public QuizDTO(Long id, String title, String text, List<String> options, QuizUser creator) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.options = options;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public QuizUser getCreator() {
        return creator;
    }

    public void setCreator(QuizUserAdapter creator) {
        this.creator = creator.getEntity();
    }
}
