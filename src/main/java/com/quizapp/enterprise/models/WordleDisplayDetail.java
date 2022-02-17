package com.quizapp.enterprise.models;

import lombok.Data;

@Data
public class WordleDisplayDetail {

    private int wordleLength;
    private int wordleTimeLimit;
    private int totalGuesses;
    private int questionId;
    private String gameId;
}
