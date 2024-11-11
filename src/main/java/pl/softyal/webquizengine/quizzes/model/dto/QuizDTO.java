package pl.softyal.webquizengine.quizzes.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.softyal.webquizengine.users.model.entity.QuizUser;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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

}
