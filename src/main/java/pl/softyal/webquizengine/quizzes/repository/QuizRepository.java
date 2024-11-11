package pl.softyal.webquizengine.quizzes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.softyal.webquizengine.quizzes.model.entity.Quiz;

public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long>, JpaRepository<Quiz, Long> {
}
