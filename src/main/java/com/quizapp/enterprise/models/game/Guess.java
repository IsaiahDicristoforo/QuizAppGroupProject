package com.quizapp.enterprise.models.game;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Guess {
    // doing it this way beacuse java doens't have nullable bools for no reason
    // -1 = not in word, 0 = wrong position, 1 = correct position
    public int IsCorrectLetter;
    public String Letter;

    private String guess;
    private long questionId;

    private String gameCode;
    private String playerName;

    public Guess(int isCorrectLetter, String letter) {
        IsCorrectLetter = isCorrectLetter;
        Letter = letter;
    }

    public Guess(){

    }
}
