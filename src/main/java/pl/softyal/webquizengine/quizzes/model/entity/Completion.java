package pl.softyal.webquizengine.quizzes.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Completion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer userId;

    private Long quizId;

    private LocalDateTime completedAt;

    public Completion(Integer userId, Long quizId, LocalDateTime completedAt) {
        this.userId = userId;
        this.quizId = quizId;
        this.completedAt = completedAt;
    }

}
