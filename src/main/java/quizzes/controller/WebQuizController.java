package quizzes.controller;

import quizzes.model.Answer;
import quizzes.model.Result;
import quizzes.model.dto.CompletionDTO;
import quizzes.model.dto.QuizDTO;
import quizzes.service.WebQuizService;
import users.model.QuizUserAdapter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
public class WebQuizController {

    WebQuizService webQuizService;

    @Autowired
    public WebQuizController(WebQuizService webQuizService) {
        this.webQuizService = webQuizService;
    }

    @GetMapping()
    public ResponseEntity<Page<QuizDTO>> getQuizzes(@RequestParam int page) {
        return new ResponseEntity<>(webQuizService.getQuizzes(page), HttpStatus.OK);
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<CompletionDTO>> getCompletions(@RequestParam int page, @AuthenticationPrincipal QuizUserAdapter user) {
        return new ResponseEntity<>(webQuizService.getCompletions(page, user), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<QuizDTO> createQuiz(@Valid @RequestBody QuizDTO quiz, @AuthenticationPrincipal QuizUserAdapter user) {
        return new ResponseEntity<>(webQuizService.createQuiz(quiz, user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long id) {
        HttpStatus responseStatus = HttpStatus.OK;
        QuizDTO quiz = webQuizService.getQuizById(id);

        if (quiz == null) {
            responseStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(quiz, responseStatus);
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<Result> solveQuiz(@PathVariable Long id, @RequestBody Answer answer, @AuthenticationPrincipal QuizUserAdapter user) {
        HttpStatus responseStatus = HttpStatus.OK;
        Result result = webQuizService.solveQuiz(id, answer, user);

        if (result == null) {
            responseStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(result, responseStatus);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id, @AuthenticationPrincipal QuizUserAdapter user) {
        HttpStatus status = webQuizService.deleteQuiz(id, user);

        return new ResponseEntity<>(status);
    }

}
