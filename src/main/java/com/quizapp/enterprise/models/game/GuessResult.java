package com.quizapp.enterprise.models.game;

import lombok.Data;

@Data
public class GuessResult {

    private String playerGuess;
    private LetterResult[] guessResults; //The length of the array should be the length of the word.
    private String playerUsername;
    private String gameId;
    private boolean wordCorrect;

}
