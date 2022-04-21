package com.quizapp.enterprise.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WordleDisplayDetail {

    private int wordleLength;
    private int wordleTimeLimit;
    private int totalGuesses;
    private int questionId;
    private String gameId;
    private List<Hint> hints;
}
