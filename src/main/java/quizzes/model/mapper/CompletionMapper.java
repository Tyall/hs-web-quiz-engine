package quizzes.model.mapper;

import quizzes.model.dto.CompletionDTO;
import quizzes.model.entity.Completion;
import org.springframework.stereotype.Component;

@Component
public class CompletionMapper {

    public CompletionDTO convertCompletionToDTO(Completion completion) {
        return new CompletionDTO(completion.getQuizId(),
                completion.getCompletedAt());
    }

}
