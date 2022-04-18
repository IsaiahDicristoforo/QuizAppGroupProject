package com.quizapp.enterprise.services;

import com.quizapp.enterprise.errorHandling.BusinessLogicError;
import com.quizapp.enterprise.events.GameOverEventPublisher;
import com.quizapp.enterprise.events.RoundOverEvent;
import com.quizapp.enterprise.events.RoundOverEventPublisher;
import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.models.game.*;
import com.quizapp.enterprise.persistence.GameTracker;
import com.quizapp.enterprise.persistence.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
        newGame.setQuestions((ArrayList<Question>) questionRepository.findByquizId(quizId));

        GameTracker.getInstance().addGame(newGame);

        return newGame;

    }

    @Override
    public ArrayList<Game> getAllGames() {
        return GameTracker.getInstance().getAllGames();
    }

    @Override
    public Game getGame(String gameId) throws BusinessLogicError {
        return GameTracker.getInstance().getGameByCode(gameId);
    }

    @Override
    public void joinGame(String gameId, Player playerToJoin) throws BusinessLogicError {

        playerToJoin.setRound(new PlayerRound());

        if(userNameExists(playerToJoin.getPlayerUsername(), gameId)){
            throw new BusinessLogicError("Username already exists. Please choose another username");
        }
        GameTracker.getInstance().joinGame(gameId, playerToJoin);
    }

    private boolean userNameExists(String userName, String gameCode) throws BusinessLogicError {

      return GameTracker
                .getInstance()
                .getGameByCode(gameCode)
                .getPlayers()
                .stream()
                .anyMatch(player -> player.getPlayerUsername()
                        .equals(userName));

    }

    public GuessResult ProcessPlayerGuess(String userGuess, String gameCode, Long questionId, String playerName, int secondsRemaining) throws BusinessLogicError {
        if(userGuess != null && !isWord(userGuess))
        {
            GuessResult gr = new GuessResult();
            gr.setInDictionary(false);
            return gr;
        }


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
       result.setInDictionary(true);

       if(wordCorrect){
           Player player = GameTracker.getInstance().getPlayer(gameCode, playerName);
           int totalPoints = (int)(((double) secondsRemaining / question.getQuestionTimeLimitSeconds()) * 100);
           player.setTotalPoints(player.getTotalPoints() +  totalPoints);
           result.setTotalPoints(totalPoints);
           GameTracker.getInstance().updatePlayerRound(gameCode, playerName, true, true, totalPoints );
       }else{
           int totalAllowedGuesses = question.getTotalGuessesAllowed();
           int guessesTaken = GameTracker.getInstance().getPlayer(gameCode, playerName).getRound().getTotalGuessesTaken();
           guessesTaken += 1;
           GameTracker.getInstance().getPlayer(gameCode, playerName).getRound().setTotalGuessesTaken(guessesTaken);

           if(totalAllowedGuesses == guessesTaken){
               GameTracker.getInstance().updatePlayerRound(gameCode, playerName, false, true, 0);
           }
       }
       GameStatus status = GameTracker.getInstance().getGameByCode(gameCode).getGameStatus();
      dispatchGameOrRoundOverEvents(status, gameCode);
       return result;
    }

    @Override
    public void processPlayerTimeExpirationEvent(String playerName, String gameId) throws BusinessLogicError {
        GameTracker.getInstance().updatePlayerRound(gameId, playerName, false, true, 0);
        dispatchGameOrRoundOverEvents(GameTracker.getInstance().getGameByCode(gameId).getGameStatus(), gameId);
    }

    private void dispatchGameOrRoundOverEvents(GameStatus gameStatus, String gameCode) throws BusinessLogicError {
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
                userGuessList.add(new Guess(1, Character.toString(userGuessArr[i])));
            } else if(correctAnswer.contains(Character.toString(userGuessArr[i]))){
                userGuessList.add(new Guess(0, Character.toString(userGuessArr[i])));
            } else {
                userGuessList.add(new Guess(-1, Character.toString(userGuessArr[i])));
            }
        }

        return userGuessList;
    }

    /***
     * Checks the user guess against the word list
     * to be verified as a word or not as a word.
     *
     * @param word the users guess
     * @return boolean (is word/is not word)
     */
    public boolean isWord(String word) {
        // Get the word file path
        Path path = Paths.get("words.txt");

        // Read the words into a byte stream
        byte[] readBytes = new byte[0];
        try {
            readBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the list to a string and set all words to lower case
        String wordListContents = new String(readBytes, StandardCharsets.UTF_8).toLowerCase();

        // Convert words to a list and add it to our hash set
        String[] words = wordListContents.split("\n");
        HashSet<String> wordsSet = new HashSet<>();
        Collections.addAll(wordsSet, words);

        // Do the comparison
        if(wordsSet.contains(word.toLowerCase())){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Question nextQuestion(String gameId) throws BusinessLogicError {
        //A new round has started, so we need to reset the game state
        GameTracker.getInstance().updateGameState(GameStatus.Started, gameId);
        return GameTracker.getInstance().getNextQuestion(gameId);
    }
}
