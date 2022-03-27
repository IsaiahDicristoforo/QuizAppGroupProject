package com.quizapp.enterprise.models;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @NotNull
    @NotBlank(message = "A question must have a word to guess")
    private String wordle;

    private int questionTimeLimitSeconds;

    private int totalGuessesAllowed;

    private int quizId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="question_id")
    private List<Hint> hints = new ArrayList<Hint>();
}
