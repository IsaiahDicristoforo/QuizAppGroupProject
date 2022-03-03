package com.quizapp.enterprise.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.Min;
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
    @Column(name = "questionid")
    private int questionId;

    @NotNull
    @NotBlank(message = "A question must have a word to guess")
    private String wordle;

    private int questionTimeLimitSeconds = 120;

    private int totalGuessesAllowed = 5;

    private int quizId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="questionid")
    private List<Hint> hints = new ArrayList<Hint>();
}
