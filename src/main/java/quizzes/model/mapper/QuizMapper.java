package quizzes.model.mapper;

import quizzes.model.dto.QuizDTO;
import quizzes.model.entity.Quiz;
import org.springframework.stereotype.Component;

@Component
public class QuizMapper {

    public QuizDTO convertQuizToDTO(Quiz quiz) {
        return new QuizDTO(quiz.getId(),
                quiz.getTitle(),
                quiz.getText(),
                quiz.getOptions(),
                quiz.getCreator());
    }

    public Quiz convertDTOToQuiz(QuizDTO quizDTO) {
        return new Quiz(quizDTO.getTitle(),
                quizDTO.getText(),
                quizDTO.getOptions(),
                quizDTO.getAnswer(),
                quizDTO.getCreator());
    }
}
