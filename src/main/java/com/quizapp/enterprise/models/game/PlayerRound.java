package com.quizapp.enterprise.models.game;


import lombok.Data;

import java.util.ArrayList;

@Data
public class PlayerRound {

    private ArrayList<Guess> guesses;
    private boolean isComplete;
    private int totalGuessesTaken;

}
