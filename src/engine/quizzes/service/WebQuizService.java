package engine.quizzes.service;

import engine.quizzes.model.Answer;
import engine.quizzes.model.Result;
import engine.quizzes.model.dto.CompletionDTO;
import engine.quizzes.model.dto.QuizDTO;
import engine.quizzes.model.entity.Completion;
import engine.quizzes.model.entity.Quiz;
import engine.quizzes.model.mapper.CompletionMapper;
import engine.quizzes.model.mapper.QuizMapper;
import engine.quizzes.repository.CompletionRepository;
import engine.quizzes.repository.QuizRepository;
import engine.users.model.QuizUserAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class WebQuizService {

    private static final Logger LOG = LoggerFactory.getLogger(WebQuizService.class);

    QuizRepository quizRepository;

    CompletionRepository completionRepository;

    QuizMapper quizMapper;

    CompletionMapper completionMapper;

    @Autowired
    public WebQuizService(QuizRepository quizRepository,
                          CompletionRepository completionRepository,
                          QuizMapper quizMapper,
                          CompletionMapper completionMapper) {
        this.quizRepository = quizRepository;
        this.completionRepository = completionRepository;
        this.quizMapper = quizMapper;
        this.completionMapper = completionMapper;
    }

    public Page<QuizDTO> getQuizzes(int page) {
        LOG.info("Getting quiz records from page {}", page);

        return quizRepository
                .findAll(PageRequest.of(page, 10))
                .map(quizMapper::convertQuizToDTO);
    }

    public QuizDTO createQuiz(QuizDTO quiz, QuizUserAdapter creator) {
        LOG.info("Trying to create quiz {} by user with email {}", quiz, creator.getUsername());
        quiz.setCreator(creator);
        Quiz savedEntity = quizRepository.save(quizMapper.convertDTOToQuiz(quiz));
        LOG.info("Quiz {} successfully created with id {}", quiz, quiz.getId());
        return quizMapper.convertQuizToDTO(savedEntity);
    }

    public QuizDTO getQuizById(Long id) {
        LOG.info("Trying to get quiz of id {}", id);

        Quiz quiz = quizRepository.findById(id).orElse(null);
        return quiz == null ? null : quizMapper.convertQuizToDTO(quiz);
    }

    public Result solveQuiz(Long id, Answer answer, QuizUserAdapter user) {
        Quiz quiz = quizRepository.getById(id);
        Result result = null;

        LOG.info("Solving quiz of id {} with provided answer {}", id, answer.getAnswer());

        result = new Result();
        boolean status = isAnswerCorrect(quiz, answer);
        result.setSuccess(status);
        result.setFeedback(status ? Result.FEEDBACK_CORRECT : Result.FEEDBACK_WRONG);

        if (status) {
            addNewCompletion(id, user.getId());
        }

        LOG.info("Solving quiz of id {}. Is answer correct: {}", id, status);

        return result;
    }

    private void addNewCompletion(Long quizId, Integer userId) {
        Completion completion  = new Completion(userId, quizId, LocalDateTime.now());
        completionRepository.save(completion);
    }

    private boolean isAnswerCorrect(Quiz quiz, Answer answer) {
        try {
            return answer.getAnswer().equals(quiz.getAnswer());
        } catch (Exception e) {
            LOG.error("Exception occurred when trying to check answer of quiz of id {} with provided answer {}", quiz.getId(), answer.getAnswer());
            return false;
        }
    }

    public HttpStatus deleteQuiz(Long id, QuizUserAdapter currentUser) {
        QuizDTO quiz = getQuizById(id);

        LOG.info("Attempting to delete quiz with id {}, quiz is {}", id, quiz);

        if (quiz == null) {
            LOG.warn("Quiz with id {} not found", id);
            return HttpStatus.NOT_FOUND;
        } else if (!Objects.equals(quiz.getCreator().getEmail(), currentUser.getUsername())) {
            LOG.warn("Attempting to delete other user's quiz with id {}. Current user email {}, quiz user email {}", id, currentUser.getUsername(), quiz.getCreator().getEmail());
            return HttpStatus.FORBIDDEN;
        } else {
            quizRepository.deleteById(id);

            LOG.info("Successfully deleted quiz with id {}", id);

            return HttpStatus.NO_CONTENT;
        }
    }

    public Page<CompletionDTO> getCompletions(int page, QuizUserAdapter user) {
        LOG.info("Getting completion records from page {} for userId {}", page, user.getId());

        return completionRepository
                .findByUserId(user.getId(), PageRequest.of(page, 10, Sort.by("completedAt").descending()))
                .map(completionMapper::convertCompletionToDTO);
    }
}
