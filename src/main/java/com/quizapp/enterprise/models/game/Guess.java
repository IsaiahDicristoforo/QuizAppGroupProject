package com.quizapp.enterprise.models.game;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Guess {
    public boolean IsCorrectLetter;
    public String Letter;

    private String guess;
    private long questionId;


    public Guess(boolean isCorrectLetter, String letter) {
        IsCorrectLetter = isCorrectLetter;
        Letter = letter;
    }

    public Guess(){

    }


}
