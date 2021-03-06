package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.errorHandling.BusinessLogicError;
import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.models.WordleDisplayDetail;
import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.Guess;
import com.quizapp.enterprise.models.game.GuessResult;
import com.quizapp.enterprise.models.game.Player;
import com.quizapp.enterprise.persistence.GameTracker;
import com.quizapp.enterprise.services.GameService;
import com.quizapp.enterprise.services.IGameService;
import com.quizapp.enterprise.services.IQuestionService;
import com.quizapp.enterprise.webSockets.PlayerJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    IGameService gameService;

    @Autowired
    IQuestionService questionService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;


    @PostMapping(value = "/newGame/{quizId}")
    public Game startNewGame(@PathVariable("quizId") int quizId){
        return gameService.startNewGame(quizId);
    }

    @GetMapping(value = "/{gameId}/players")
    public ArrayList<Player> getPlayers(@PathVariable("gameId") String gameId) throws BusinessLogicError {
        return gameService.getGame(gameId).getPlayers();
    }

    @GetMapping(value = "/{gameId}")
    public Game getGame(@PathVariable("gameId") String gameId) throws BusinessLogicError {
      return gameService.getGame(gameId);
    }

    @PostMapping(value = "/joinGame/{gameId}")
    public ResponseEntity<Game> joinGame(@PathVariable("gameId") String gameId, @RequestBody Player player) throws BusinessLogicError {
        gameService.joinGame(gameId, player);
        return new ResponseEntity<Game>(gameService.getGame(gameId), HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/nextQuestion")
    public Question nextQuestion(@PathVariable("id") String gameId) throws BusinessLogicError {
        return gameService.nextQuestion(gameId);
    }

    @GetMapping("")
    public ArrayList<Game> getAllGames(){
        return gameService.getAllGames();
    }

    @PostMapping("/checkGuess")
    public GuessResult checkGuess(@RequestBody Guess userGuess) throws Exception {
        GuessResult result = gameService.ProcessPlayerGuess(userGuess.getGuess(),userGuess.getGameCode(), userGuess.getQuestionId(), userGuess.getPlayerName(), userGuess.getSecondsRemaining());
        result.setGameId(userGuess.getGameCode());
        result.setPlayerUsername(userGuess.getPlayerName());

        if(result.isWordCorrect() || GameTracker.getInstance().getPlayer(userGuess.getGameCode(), userGuess.getPlayerName()).getRound().getTotalGuessesTaken() >= questionService.getQuestion((int) userGuess.getQuestionId()).getTotalGuessesAllowed()){
            sendGuessResult(result);
        }
        return result;
    }

    @PostMapping("/{gameId}/timeUp")
    public void playerFailEvent(@RequestParam("playerName") String playerName, @PathVariable("gameId") String gameId) throws BusinessLogicError {
        gameService.processPlayerTimeExpirationEvent(playerName, gameId);
    }

    @GetMapping("/{gameID}/gameExists")
    public boolean checkIfGameExists(@PathVariable("gameID") String gameId){
        return GameTracker.getInstance().gameExists(gameId);
    }

    @MessageMapping("/gameOver")
    public void sendGameOverNotification(String gameId) throws BusinessLogicError {
        ArrayList<Player> leaderboard = GameTracker.getInstance().getLeaderboard(gameId);
        messagingTemplate.convertAndSend("/game1/gameOver/3" , leaderboard );
    }

    @MessageMapping("/playerUpdate")
    public GuessResult sendGuessResult(GuessResult result){
        messagingTemplate.convertAndSend("/game1/playerUpdate/" + result.getGameId(), result);
        return result;
    }

    @MessageMapping("/chat")
    public PlayerJoinEvent send(PlayerJoinEvent message) throws Exception {
        Player newPlayer = new Player();
        newPlayer.setTotalPoints(0);
        newPlayer.setPlayerUsername(message.getPlayerName());
        newPlayer.setHost(false);
        gameService.joinGame(message.getGameId(), newPlayer);
        messagingTemplate.convertAndSend("/game1/messages/" + message.getGameId(), message);
        return message;
    }

    @MessageMapping("/chat1/nextQuestion")
    public void send(WordleDisplayDetail wordleDisplayDetails) throws Exception {
        Question newQuestion  = gameService.nextQuestion(wordleDisplayDetails.getGameId());
        wordleDisplayDetails.setWordleLength(newQuestion.getWordle().length());
        wordleDisplayDetails.setQuestionId(newQuestion.getQuestionId().intValue());
        wordleDisplayDetails.setTotalGuesses(newQuestion.getTotalGuessesAllowed());
        wordleDisplayDetails.setWordleTimeLimit(newQuestion.getQuestionTimeLimitSeconds());
        wordleDisplayDetails.setHints(newQuestion.getHints());
        messagingTemplate.convertAndSend("/game1/newQuestion/"  + wordleDisplayDetails.getGameId(), wordleDisplayDetails);

    }

}
