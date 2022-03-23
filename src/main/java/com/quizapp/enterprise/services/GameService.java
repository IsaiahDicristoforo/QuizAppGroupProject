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
    public Game startNewGame(int quizId) throws Exception {

        ArrayList<Question> questions = (ArrayList<Question>) questionRepository.findByquizId(quizId);
        if(questions == null){
            throw new Exception("quiz not found");
        }

        Game newGame = new Game();
        newGame.setGameCode(UUID
                .randomUUID()
                .toString()
                .substring(0, 5));
        newGame.setGameStatus(GameStatus.Started);
        newGame.setQuizId(quizId);
        newGame.setQuestions(questions);

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

    public GuessResult ProcessPlayerGuess(String userGuess,
                                          String gameCode,
                                          Long questionId,
                                          String playerName) throws Exception {

        Question question = questionRepository.getById(questionId);
        if(question == null){
            throw new Exception("question not found");
        }

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

    /***
     * Checks the user guess agains the correct answer and return ArrayList<Guess>

     * to be verified and displayed client side.
     *
     * @param userGuess the users guess
     * @param correctAnswer the correct answer
     * @return ArrayList<Guess> (bool, string)
     */
    public ArrayList<Guess> checkGuess(String userGuess, String correctAnswer) {
        // Convert guess and correct guess to a character array
        char[] userGuessArr = userGuess.toCharArray();
        char[] correctAnswerArr = correctAnswer.toCharArray();

        // Initialize the list of guesses with their respective correctness
        ArrayList<Guess> userGuessList = new ArrayList<Guess>();

        // Loop over the characters in the guess
        for(int i = 0; i < userGuess.length(); i++) {
            // If guess character == correct character, letter = correct
            if(userGuessArr[i] == correctAnswerArr[i]){
                //userGuessList.add(new Guess(true, Character.toString(userGuessArr[i])));
            } else {
                //userGuessList.add(new Guess(false, Character.toString(userGuessArr[i])));
            }
        }
        return userGuessList;
    }

    @Override
    public Question nextQuestion(String gameId) throws Exception {
        //A new round has started, so we need to reset the game state
        GameTracker.getInstance().updateGameState(GameStatus.Started, gameId);
        return GameTracker.getInstance().getNextQuestion(gameId);
    }
}
