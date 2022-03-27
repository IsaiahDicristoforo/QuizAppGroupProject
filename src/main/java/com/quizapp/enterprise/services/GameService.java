package com.quizapp.enterprise.services;

import com.quizapp.enterprise.events.GameOverEventPublisher;
import com.quizapp.enterprise.events.RoundOverEvent;
import com.quizapp.enterprise.events.RoundOverEventPublisher;
import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.models.game.*;
import com.quizapp.enterprise.persistence.GameTracker;
import com.quizapp.enterprise.persistence.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class GameService implements IGameService{

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RoundOverEventPublisher roundOverEventPublisher;

    @Autowired private GameOverEventPublisher gameOverEventPublisher;

    @Override
    public Game startNewGame(int quizId) {

        Game newGame = new Game();
        newGame.setGameCode(UUID
                .randomUUID()
                .toString()
                .substring(0, 5));
        newGame.setGameStatus(GameStatus.Started);
        newGame.setQuizId(quizId);
        newGame.setQuestions((ArrayList<Question>) questionRepository.findByQuizId(quizId));

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

        playerToJoin.setRound(new PlayerRound());

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

    public GuessResult ProcessPlayerGuess(String userGuess, String gameCode, Long questionId, String playerName) {

        Question question = questionRepository.getById(questionId);
        String correctWord = question.getWordle();

        GuessResult result = new GuessResult();

       char[] userLetters = userGuess.toCharArray();

       LetterResult[] wordResults = new LetterResult[userGuess.length()];

       boolean wordCorrect = true; //Tracks if the whole word is correct, not just the individual letters.

       for(int i = 0; i < userLetters.length; i++){
           if(userLetters[i] == correctWord.charAt(i)){
               wordResults[i] = LetterResult.Correct;

           }else if(correctWord.contains(Character.toString(userLetters[i]))){
               wordResults[i] = LetterResult.WrongLocation;
               wordCorrect = false;
           }else{
               wordResults[i] = LetterResult.NotInWord;
               wordCorrect = false;
           }
       }
       result.setGuessResults(wordResults);
       result.setWordCorrect(wordCorrect);

       if(wordCorrect){
           GameTracker.getInstance().updatePlayerRound(gameCode, playerName, true, true);
           Player player = GameTracker.getInstance().getPlayer(gameCode, playerName);
           player.setTotalPoints(player.getTotalPoints() + 1000);
       }else{


           int totalAllowedGuesses = question.getTotalGuessesAllowed();
           int guessesTaken = GameTracker.getInstance().getPlayer(gameCode, playerName).getRound().getTotalGuessesTaken();
           guessesTaken += 1;
           GameTracker.getInstance().getPlayer(gameCode, playerName).getRound().setTotalGuessesTaken(guessesTaken);

           if(totalAllowedGuesses == guessesTaken){
               GameTracker.getInstance().updatePlayerRound(gameCode, playerName, false, true);
           }
       }

       GameStatus status = GameTracker.getInstance().getGameByCode(gameCode).getGameStatus();
      dispatchGameOrRoundOverEvents(status, gameCode);
       return result;
    }

    @Override
    public void processPlayerTimeExpirationEvent(String playerName, String gameId) {
        GameTracker.getInstance().updatePlayerRound(gameId, playerName, false, true);
        dispatchGameOrRoundOverEvents(GameTracker.getInstance().getGameByCode(gameId).getGameStatus(), gameId);
    }

    private void dispatchGameOrRoundOverEvents(GameStatus gameStatus, String gameCode){
        if(gameStatus == GameStatus.GameEnded){
            gameOverEventPublisher.publishGameOverEvent(gameCode, GameTracker.getInstance().getLeaderboard(gameCode));
            roundOverEventPublisher.publishRoundOverEvent(gameCode, GameTracker.getInstance().getLeaderboard(gameCode));

        }else if(gameStatus == GameStatus.RoundEnded){
            roundOverEventPublisher.publishRoundOverEvent(gameCode, GameTracker.getInstance().getLeaderboard(gameCode));
        }
    }

    @Override
    public Question nextQuestion(String gameId) {
        //A new round has started, so we need to reset the game state
        GameTracker.getInstance().updateGameState(GameStatus.Started, gameId);
        return GameTracker.getInstance().getNextQuestion(gameId);
    }
}
