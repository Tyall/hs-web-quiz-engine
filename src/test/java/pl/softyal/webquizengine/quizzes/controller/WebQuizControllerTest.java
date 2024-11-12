package pl.softyal.webquizengine.quizzes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.softyal.webquizengine.quizzes.model.Answer;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebQuizControllerTest {

    @MockBean
    private QuizRepository quizRepository;

    @MockBean
    private CompletionRepository completionRepository;

    @MockBean
    private QuizUserDetailsServiceImpl quizUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizMapper quizMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("GET /api/quizzes returns 200 OK and paginated results of quiz entries")
    void getQuizzes_returnOkAndPaginatedResult() throws Exception {
        Quiz quizFirst = new Quiz();
        quizFirst.setId(1L);

        Quiz quizSecond = new Quiz();
        quizSecond.setId(2L);

        List<Quiz> quizzes = Arrays.asList(quizFirst, quizSecond);
        Page<Quiz> pagedResult = new PageImpl<>(quizzes, PageRequest.of(1, 2), quizzes.size());

        Mockito.when(quizRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(pagedResult);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/quizzes").contentType(MediaType.APPLICATION_JSON).param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2));
    }

    @Test
    @DisplayName("GET /api/quizzes returns 401 Unauthorized")
    void getQuizzes_returnUnauthorized() throws Exception {
        List<Quiz> quizzes = Arrays.asList(new Quiz(), new Quiz());
        Page<Quiz> pagedResult = new PageImpl<>(quizzes, PageRequest.of(1, 2), quizzes.size());

        Mockito.when(quizRepository.findAll(Mockito.any(PageRequest.class)))
                .thenReturn(pagedResult);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/quizzes").contentType(MediaType.APPLICATION_JSON).param("page", "1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@email.com")
    @DisplayName("GET /api/quizzes/completions returns 200 OK and paginated result of completions")
    void getCompletions_returnOkAndPaginatedResult() throws Exception {
        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        Completion completionFirst = new Completion();
        completionFirst.setQuizId(1L);

        Completion completionSecond = new Completion();
        completionSecond.setQuizId(2L);

        List<Completion> completions = Arrays.asList(completionFirst, completionSecond);
        Page<Completion> pagedResult = new PageImpl<>(completions, PageRequest.of(1, 2), completions.size());

        Mockito.when(quizUserDetailsService.loadUserByUsername("test@email.com"))
                .thenReturn(userAdapter);
        Mockito.when(completionRepository.findByUserId(Mockito.anyInt(), Mockito.any(PageRequest.class)))
                .thenReturn(pagedResult);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/quizzes/completed")
                        .with(user(userAdapter))
                        .contentType(MediaType.APPLICATION_JSON).param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2));
    }

    @Test
    @WithMockUser(username = "test@email.com")
    @DisplayName("POST /api/quizzes creates new quiz and returns 200 OK")
    void createQuiz_returnOk() throws Exception {
        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        QuizDTO newQuiz = new QuizDTO(1L, "Sample title", "Sample text", List.of("A", "B"), user);

        Mockito.when(quizRepository.save(Mockito.any(Quiz.class)))
                .thenReturn(quizMapper.convertDTOToQuiz(newQuiz));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/quizzes")
                        .with(user(userAdapter))
                        .content(objectMapper.writeValueAsString(newQuiz))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/quizzes/{id} returns 200 OK and quizDTO")
    void getQuizById_returnOkAndQuizDTO() throws Exception {
        Mockito.when(quizRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new Quiz("Sample title", "Sample text", List.of("1", "2"), List.of(1), new QuizUser())));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/quizzes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample title"))
                .andExpect(jsonPath("$.text").value("Sample text"))
                .andExpect(jsonPath("$.options[0]").value("1"))
                .andExpect(jsonPath("$.options[1]").value("2"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/quizzes/{id} returns 404 NOT FOUND")
    void getQuizById_returnNotFound() throws Exception {
        Mockito.when(quizRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/quizzes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/quizzes/{id}/solve returns OK and correct answer response")
    void solveQuiz_quizFound_answerCorrect() throws Exception {
        Answer answer = new Answer(List.of(1));

        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        Mockito.when(quizRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(new Quiz("Sample title", "Sample text", List.of("1", "2"), List.of(1), new QuizUser()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/quizzes/1/solve")
                        .with(user(userAdapter))
                        .content(objectMapper.writeValueAsString(answer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/quizzes/{id}/solve returns OK and incorrect answer response")
    void solveQuiz_quizFound_answerIncorrect() throws Exception {
        Answer answer = new Answer(List.of(2));

        Mockito.when(quizRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(new Quiz("Sample title", "Sample text", List.of("1", "2"), List.of(1), new QuizUser()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/quizzes/1/solve")
                        .content(objectMapper.writeValueAsString(answer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/quizzes/{id}/solve returns NOT FOUND")
    void solveQuiz_quizNotFound() throws Exception {
        Answer answer = new Answer(List.of(2));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/quizzes/1/solve")
                        .content(objectMapper.writeValueAsString(answer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/quizzes/{id} successfully deletes and returns NO CONTENT")
    void deleteQuiz_successful() throws Exception {
        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        Quiz quiz = new Quiz("Sample title", "Sample text", List.of("A", "B"), List.of(1), user);

        Mockito.when(quizRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(quiz));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/quizzes/1")
                        .with(user(userAdapter)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/quizzes/{id} not as quiz owner and returns FORBIDDEN")
    void deleteQuiz_forbiddenAccess() throws Exception {
        QuizUser creator = new QuizUser(1, "test-creator@email.com", "password");

        QuizUser accessingUser = new QuizUser(2, "test-accessing@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(accessingUser);

        Quiz quiz = new Quiz("Sample title", "Sample text", List.of("A", "B"), List.of(1), creator);

        Mockito.when(quizRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(quiz));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/quizzes/1")
                        .with(user(userAdapter)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/quizzes/{id} not existing quiz and returns NOT FOUND")
    void deleteQuiz_quizNotFound() throws Exception {
        QuizUser user = new QuizUser(1, "test@email.com", "password");
        QuizUserAdapter userAdapter = new QuizUserAdapter(user);

        Mockito.when(quizRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/quizzes/1")
                        .with(user(userAdapter)))
                .andExpect(status().isNotFound());
    }
}