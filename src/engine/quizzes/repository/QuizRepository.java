package engine.quizzes.repository;

import engine.quizzes.model.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long>, JpaRepository<Quiz, Long> {
}
