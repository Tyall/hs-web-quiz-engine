package pl.softyal.webquizengine.quizzes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.softyal.webquizengine.quizzes.model.entity.Completion;

public interface CompletionRepository extends PagingAndSortingRepository<Completion, Long>, JpaRepository<Completion, Long> {

    Page<Completion> findByUserId(Integer userId, Pageable pageable);

}
