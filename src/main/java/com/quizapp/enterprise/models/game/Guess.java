package com.quizapp.enterprise.models.game;

public class Guess {
    boolean IsCorrectLetter;
    String Letter;

    public Guess(boolean isCorrectLetter, String letter) {
        IsCorrectLetter = isCorrectLetter;
        Letter = letter;
    }
}
