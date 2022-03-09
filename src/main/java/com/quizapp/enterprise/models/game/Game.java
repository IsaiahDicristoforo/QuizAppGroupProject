package com.quizapp.enterprise.models.game;

import com.quizapp.enterprise.models.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    private String gameCode;
    private int quizId;
    private ArrayList<Player> players = new ArrayList<>();
    private GameStatus gameStatus;
    private ArrayList<Question> questions;
    private int currentQuestionNumber;

}
