package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.GameStatus;
import com.quizapp.enterprise.models.game.Guess;
import com.quizapp.enterprise.models.game.Player;
import com.quizapp.enterprise.persistence.GameTracker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class GameService implements IGameService{
    @Override
    public Game startNewGame(int quizId) {

        Game newGame = new Game();
        newGame.setGameCode(UUID
                .randomUUID()
                .toString()
                .substring(0, 5));
        newGame.setGameStatus(GameStatus.Started);
        newGame.setQuizId(quizId);

        GameTracker.getInstance().addGame(newGame);

        return newGame;

    }

    @Override
    public ArrayList<Game> getAllGames() {
        return GameTracker.getInstance().getAllGames();
    }

    @Override
    public Game getGame(String gameId) {
        return GameTracker.getInstance().getGameByCode(gameId);
    }

    @Override
    public void joinGame(String gameId, Player playerToJoin) throws Exception {

        if(userNameExists(playerToJoin.getPlayerUsername(), gameId)){
            throw new Exception("Username already exists. Please choose another username");
        }
        GameTracker.getInstance().joinGame(gameId, playerToJoin);
    }

    private boolean userNameExists(String userName, String gameCode){
        return GameTracker
                .getInstance()
                .getGameByCode(gameCode)
                .getPlayers()
                .stream()
                .anyMatch(player -> player.getPlayerUsername()
                        .equals(userName));
    }

    /***
     * Checks the user guess agains the correct answer and return ArrayList<Guess>
     * to be verified and displayed client side.
     *
     * @param userGuess the users guess
     * @param correctAnswer the correct answer
     * @return ArrayList<Guest> (bool, string)
     */
    public ArrayList<Guess> checkGuess(String userGuess, String correctAnswer) {
        // Convert guess and correct guess to a character array
        var userGuessArr = userGuess.toCharArray();
        var correctAnswerArr = correctAnswer.toCharArray();

        // Initialize the list of guesses with their respective correctness
        var userGuessList = new ArrayList<Guess>();

        // Loop over the characters in the guess
        for(int i = 0; i < userGuess.length(); i++) {
            // If guess character == correct character, letter = correct
            if(userGuessArr[i] == correctAnswerArr[i]){
                userGuessList.add(new Guess(true, Character.toString(userGuessArr[i])));
            } else {
                userGuessList.add(new Guess(false, Character.toString(userGuessArr[i])));
            }
        }

        return userGuessList;
    }
}
