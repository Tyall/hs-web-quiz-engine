package pl.softyal.webquizengine.quizzes.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import pl.softyal.webquizengine.quizzes.exception.QuizDeletionForbiddenException;
import pl.softyal.webquizengine.quizzes.exception.QuizNotFoundException;
import pl.softyal.webquizengine.quizzes.model.Answer;
import pl.softyal.webquizengine.quizzes.model.Result;
import pl.softyal.webquizengine.quizzes.model.dto.CompletionDTO;
import pl.softyal.webquizengine.quizzes.model.dto.QuizDTO;
import pl.softyal.webquizengine.quizzes.model.entity.Completion;
import pl.softyal.webquizengine.quizzes.model.entity.Quiz;
import pl.softyal.webquizengine.quizzes.model.mapper.QuizMapper;
import pl.softyal.webquizengine.quizzes.repository.CompletionRepository;
import pl.softyal.webquizengine.quizzes.repository.QuizRepository;
import pl.softyal.webquizengine.users.model.QuizUserAdapter;
import pl.softyal.webquizengine.users.model.entity.QuizUser;
import pl.softyal.webquizengine.users.service.QuizUserDetailsServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebQuizServiceTest {

    @MockBean
    private QuizRepository quizRepository;

    @MockBean
    private CompletionRepository completionRepository;

    @MockBean
    private QuizUserDetailsServiceImpl quizUserDetailsService;

    @Autowired
    private QuizMapper quizMapper;

    @Autowired
    private WebQuizService webQuizService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Given page number returns paginated result of quizzes")
    void getQuizzes_returnPaginatedResult() {
        Quiz quizFirst = new Quiz();
        quizFirst.setId(1L);

        Quiz quizSecond = new Quiz();
        quizSecond.setId(2L);

        List<Quiz> mockQuizzes = Arrays.asList(quizFirst, quizSecond);
        Page<Quiz> mockPagedResult = new PageImpl<>(mockQuizzes, PageRequest.of(1, 2), mockQuizzes.size());

        Mockito.when(quizRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(mockPagedResult);

        Page<QuizDTO> pagedResult = webQuizService.getQuizzes(1);

        assertAll("Grouped assertions of quizzes paginated result",
                () -> assertEquals(pagedResult.getNumberOfElements(), 2, "Result should have 2 elements"),
                () -> assertEquals(pagedResult.getPageable().getPageNumber(), 1, "Result should be the first page"),
                () -> assertEquals(pagedResult.getPageable().getPageSize(), 2, "Result page should have size of 2"),
                () -> assertEquals(pagedResult.getContent().get(0).getId(), 1, "First element of result's content should have id of 1"),
                () -> assertEquals(pagedResult.getContent().get(1).getId(), 2, "Second element of result's content should have id of 2"));
    }

    @Test
    @DisplayName("Given QuizDTO and UserDetails creates and saves quiz entity")
    void createQuiz_successfully() {
        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        QuizDTO newQuiz = new QuizDTO(1L, "Sample title", "Sample text", List.of("A", "B"), user);

        Mockito.when(quizRepository.save(Mockito.any(Quiz.class)))
                .thenReturn(quizMapper.convertDTOToQuiz(newQuiz));

        QuizDTO createdQuizDTO = webQuizService.createQuiz(newQuiz, userAdapter);

        assertAll("Grouped assertions of QuizDTO created after saving quiz entity",
                () -> assertEquals(createdQuizDTO.getTitle(), "Sample title", "Result should have 2 elements"),
                () -> assertEquals(createdQuizDTO.getCreator(), user, "Result should be the first page"));
    }

    @Test
    @DisplayName("Given id of existing quiz returns QuizDTO of found entity")
    void getQuizById_returnsFoundQuiz() {
        Mockito.when(quizRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new Quiz("Sample title", "Sample text", List.of("1", "2"), List.of(1), new QuizUser())));

        QuizDTO foundQuizDTO = webQuizService.getQuizById(1L);

        assertThat(foundQuizDTO.getTitle()).isEqualTo("Sample title");
    }

    @Test
    @DisplayName("Given id of non existing quiz returns null due to entity not found")
    void getQuizById_quizNotFound_returnsEmptyQuiz() {
        Mockito.when(quizRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        QuizDTO foundQuizDTO = webQuizService.getQuizById(1L);

        assertThat(foundQuizDTO).isNull();
    }

    @Test
    @DisplayName("Given id of existing quiz and correct answer returns result with status success")
    void solveQuiz_quizFound_answerCorrect() {
        Mockito.when(quizRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(new Quiz("Sample title", "Sample text", List.of("1", "2"), List.of(1), new QuizUser()));

        Answer answer = new Answer(List.of(1));

        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        Result result = webQuizService.solveQuiz(1L, answer, userAdapter);

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("Given id of existing quiz and incorrect answer returns result with status unsuccessful")
    void solveQuiz_quizFound_answerIncorrect() {

        Answer answer = new Answer(List.of(2));

        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        Mockito.when(quizRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(new Quiz("Sample title", "Sample text", List.of("1", "2"), List.of(1), new QuizUser()));

        Result result = webQuizService.solveQuiz(1L, answer, userAdapter);

        assertThat(result.isSuccess()).isFalse();
    }

    @Test
    @DisplayName("Given id of non existing quiz and answer returns null")
    void solveQuiz_quizNotFound() {
        Answer answer = new Answer(List.of(1));

        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        Result result = webQuizService.solveQuiz(1L, answer, userAdapter);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Given id of existing quiz and creator's UserDetails deletes quiz entity successfully")
    void deleteQuiz_success() {
        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        Quiz quiz = new Quiz("Sample title", "Sample text", List.of("A", "B"), List.of(1), user);

        Mockito.when(quizRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(quiz));

        assertDoesNotThrow(() -> webQuizService.deleteQuiz(1L, userAdapter));
    }

    @Test
    @DisplayName("Given id of non existing quiz and creator's UserDetails throws QuizNotFoundException")
    void deleteQuiz_incorrectId_quizNotFoundException() {
        QuizUser creator = new QuizUser(1, "test-creator@email.com", "password");

        QuizUser accessingUser = new QuizUser(2, "test-accessing@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(accessingUser);

        Quiz quiz = new Quiz("Sample title", "Sample text", List.of("A", "B"), List.of(1), creator);

        Mockito.when(quizRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(quiz));

        assertThrows(QuizDeletionForbiddenException.class,
                () -> webQuizService.deleteQuiz(1L, userAdapter));
    }

    @Test
    @DisplayName("Given id of existing quiz and not creator's UserDetails throws QuizDeletionForbiddenException")
    void deleteQuiz_incorrectCreator_quizDeletionForbiddenException() {
        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        Mockito.when(quizRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(QuizNotFoundException.class,
                () -> webQuizService.deleteQuiz(1L, userAdapter));
    }

    @Test
    @DisplayName("Given page number and valid User Details returns paginated result of completions")
    void getCompletions_returnPaginatedResult() {
        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        Completion completionFirst = new Completion();
        completionFirst.setQuizId(1L);

        Completion completionSecond = new Completion();
        completionSecond.setQuizId(2L);

        List<Completion> mockCompletions = Arrays.asList(completionFirst, completionSecond);
        Page<Completion> mockPagedResult = new PageImpl<>(mockCompletions, PageRequest.of(1, 2), mockCompletions.size());

        Mockito.when(quizUserDetailsService.loadUserByUsername("test@email.com"))
                .thenReturn(userAdapter);
        Mockito.when(completionRepository.findByUserId(Mockito.anyInt(), Mockito.any(PageRequest.class)))
                .thenReturn(mockPagedResult);

        Page<CompletionDTO> pagedResult = webQuizService.getCompletions(1, userAdapter);

        assertAll("Grouped assertions of completions paginated result",
                () -> assertEquals(pagedResult.getNumberOfElements(), 2, "Result should have 2 elements"),
                () -> assertEquals(pagedResult.getPageable().getPageNumber(), 1, "Result should be the first page"),
                () -> assertEquals(pagedResult.getPageable().getPageSize(), 2, "Result page should have size of 2"),
                () -> assertEquals(pagedResult.getContent().get(0).getId(), 1, "First element of result's content should have id of 1"),
                () -> assertEquals(pagedResult.getContent().get(1).getId(), 2, "Second element of result's content should have id of 2"));
    }
}