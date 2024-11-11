package pl.softyal.webquizengine.quizzes.model.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.softyal.webquizengine.quizzes.model.dto.QuizDTO;
import pl.softyal.webquizengine.quizzes.model.entity.Quiz;
import pl.softyal.webquizengine.users.model.entity.QuizUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuizMapperTest {

    private final QuizMapper quizMapper;

    @Autowired
    public QuizMapperTest(QuizMapper quizMapper) {
        this.quizMapper = quizMapper;
    }

    @Test
    @DisplayName("convert quiz entity to dto")
    void convertQuizToDTO() {
        QuizUser creator = new QuizUser();

        Quiz quiz = new Quiz("Test quiz",
                "Sample text",
                List.of("A", "B"),
                List.of(1),
                creator);

        quiz.setId(1L);

        QuizDTO convertedDTO = quizMapper.convertQuizToDTO(quiz);

        assertAll("Grouped assertions of QuizDTO",
                () -> assertEquals(1L, convertedDTO.getId(), "QuizDTO title should have id 1"),
                () -> assertEquals("Test quiz", convertedDTO.getTitle(), "QuizDTO title should be 'Test quiz'"),
                () -> assertEquals("Sample text", convertedDTO.getText(), "QuizDTO text should be 'Sample text'"),
                () -> assertEquals(List.of("A", "B"), convertedDTO.getOptions(), "QuizDTO should have options A and B"),
                () -> assertEquals(creator, convertedDTO.getCreator(), "QuizDTO should have the same creator as quiz entity")
        );
    }

    @Test
    @DisplayName("convert quiz dto to entity")
    void convertDTOToQuiz() {
        QuizUser creator = new QuizUser();
        Quiz quiz = quizMapper.convertDTOToQuiz(new QuizDTO(1L,
                "Test quiz",
                "Sample text",
                List.of("A", "B"),
                creator));

        assertAll("Grouped assertions of Quiz",
                () -> assertEquals("Test quiz", quiz.getTitle(), "Quiz title should be 'Test quiz'"),
                () -> assertEquals("Sample text", quiz.getText(), "Quiz text should be 'Sample text'"),
                () -> assertEquals(List.of("A", "B"), quiz.getOptions(), "Quiz should have options A and B"),
                () -> assertEquals(creator, quiz.getCreator(), "Quiz creator should be user")
        );
    }
}