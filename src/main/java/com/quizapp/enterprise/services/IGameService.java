package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.Guess;
import com.quizapp.enterprise.models.game.GuessResult;
import com.quizapp.enterprise.models.game.Player;

import java.util.ArrayList;

public interface IGameService {

     Game startNewGame(int quizId);
     ArrayList<Game> getAllGames();
     Game getGame(String gameId);
     void joinGame(String gameId, Player playerToJoin) throws Exception;
     ArrayList<Guess> checkGuess(String userGuess, String correctAnswer);
     Question nextQuestion(String gameId);
     GuessResult GetGuessResult(String userGuess, Long questionId);
}
