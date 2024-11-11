package pl.softyal.webquizengine.quizzes.model.mapper;

import org.springframework.stereotype.Component;
import pl.softyal.webquizengine.quizzes.model.dto.CompletionDTO;
import pl.softyal.webquizengine.quizzes.model.entity.Completion;

@Component
public class CompletionMapper {

    public CompletionDTO convertCompletionToDTO(Completion completion) {
        return new CompletionDTO(completion.getQuizId(),
                completion.getCompletedAt());
    }

}
