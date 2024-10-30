package engine.quizzes.repository;

import engine.quizzes.model.entity.Completion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletionRepository extends PagingAndSortingRepository<Completion, Long>, JpaRepository<Completion, Long> {

    Page<Completion> findByUserId(Integer userId, Pageable pageable);

}
