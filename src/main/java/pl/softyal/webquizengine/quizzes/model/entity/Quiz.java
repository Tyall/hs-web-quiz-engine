package pl.softyal.webquizengine.quizzes.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pl.softyal.webquizengine.users.model.entity.QuizUser;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> options;

    @Fetch(value = FetchMode.SUBSELECT)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> answer;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private QuizUser creator;

    public Quiz(String title, String text, List<String> options, List<Integer> answer, QuizUser creator) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.creator = creator;
    }

}



