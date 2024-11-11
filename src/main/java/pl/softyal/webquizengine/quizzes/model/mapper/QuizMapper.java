package pl.softyal.webquizengine.quizzes.model.mapper;

import org.springframework.stereotype.Component;
import pl.softyal.webquizengine.quizzes.model.dto.QuizDTO;
import pl.softyal.webquizengine.quizzes.model.entity.Quiz;

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
