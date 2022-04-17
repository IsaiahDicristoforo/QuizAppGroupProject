package com.quizapp.enterprise.services;

import com.quizapp.enterprise.errorHandling.BusinessLogicError;
import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.Guess;
import com.quizapp.enterprise.models.game.GuessResult;
import com.quizapp.enterprise.models.game.Player;

import java.util.ArrayList;

public interface IGameService {

     Game startNewGame(int quizId);
     ArrayList<Game> getAllGames();
     Game getGame(String gameId) throws BusinessLogicError;
     void joinGame(String gameId, Player playerToJoin) throws BusinessLogicError;
     ArrayList<Guess> checkGuess(String userGuess, String correctAnswer);
     Question nextQuestion(String gameId) throws BusinessLogicError;
     GuessResult ProcessPlayerGuess(String userGuess, String gameCode, Long questionId, String playerName, int secondsRemaining ) throws BusinessLogicError;
     void processPlayerTimeExpirationEvent(String playerName, String gameId) throws BusinessLogicError;
}
