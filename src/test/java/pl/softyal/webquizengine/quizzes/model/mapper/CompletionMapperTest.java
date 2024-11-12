package pl.softyal.webquizengine.quizzes.model.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.softyal.webquizengine.quizzes.model.dto.CompletionDTO;
import pl.softyal.webquizengine.quizzes.model.entity.Completion;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompletionMapperTest {

    private final CompletionMapper completionMapper;

    @Autowired
    public CompletionMapperTest(CompletionMapper completionMapper) {
        this.completionMapper = completionMapper;
    }

    @Test
    @DisplayName("convert completion entity to dto")
    void convertCompletionToDTO() {
        LocalDateTime completedAt = LocalDateTime.now();

        CompletionDTO dto = completionMapper.convertCompletionToDTO(new Completion(0, 1L, completedAt));

        assertAll("Grouped assertions of CompletionDTO",
                () -> assertEquals(1L, dto.getId(), "CompletionDTO should have id 1"),
                () -> assertEquals(completedAt, dto.getCompletedAt(), "CompletionDTO should have the same completion date as completion entity"));
    }
}