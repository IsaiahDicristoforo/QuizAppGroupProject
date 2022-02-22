package com.quizapp.enterprise.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Data
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

    private int questionTimeLimit = 120;

    private int attemptsRemaining = 5;

    private int quizId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="questionid")
    private List<Hint> hints = new ArrayList<>();
}
