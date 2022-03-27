package com.quizapp.enterprise.models.game;

public class Guess {
    public boolean IsCorrectLetter;
    public String Letter;

    public Guess(Boolean isCorrectLetter, String letter) {
        IsCorrectLetter = isCorrectLetter;
        Letter = letter;
    }
}
