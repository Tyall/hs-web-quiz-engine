package pl.softyal.webquizengine.quizzes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CompletionDTO {

    private Long id;
    private LocalDateTime completedAt;

}
