package users.repository;

import users.model.entity.QuizUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizUserRepository extends JpaRepository<QuizUser, Integer> {
    Optional<QuizUser> findQuizUserByEmail(String email);

    boolean existsByEmail(String email);

}
