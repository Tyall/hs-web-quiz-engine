package pl.softyal.webquizengine.quizzes.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pl.softyal.webquizengine.users.model.entity.QuizUser;

import java.util.List;

@Entity
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

    public Quiz() {
    }

    public Quiz(String title, String text, List<String> options, List<Integer> answer, QuizUser creator) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public QuizUser getCreator() {
        return creator;
    }

    public void setCreator(QuizUser creator) {
        this.creator = creator;
    }
}
