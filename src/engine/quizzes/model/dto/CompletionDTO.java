package engine.quizzes.model.dto;

import java.time.LocalDateTime;

public class CompletionDTO {

    private Long id;

    private LocalDateTime completedAt;

    public CompletionDTO(Long id, LocalDateTime completedAt) {
        this.id = id;
        this.completedAt = completedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
