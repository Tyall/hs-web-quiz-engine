package engine.quizzes.model.mapper;

import engine.quizzes.model.dto.CompletionDTO;
import engine.quizzes.model.entity.Completion;
import org.springframework.stereotype.Component;

@Component
public class CompletionMapper {

    public CompletionDTO convertCompletionToDTO(Completion completion) {
        return new CompletionDTO(completion.getQuizId(),
                completion.getCompletedAt());
    }

}
