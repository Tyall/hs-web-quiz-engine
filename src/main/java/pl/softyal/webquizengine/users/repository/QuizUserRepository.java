package pl.softyal.webquizengine.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.softyal.webquizengine.users.model.entity.QuizUser;

import java.util.Optional;

public interface QuizUserRepository extends JpaRepository<QuizUser, Integer> {

    Optional<QuizUser> findQuizUserByEmail(String email);

    boolean existsByEmail(String email);

}
