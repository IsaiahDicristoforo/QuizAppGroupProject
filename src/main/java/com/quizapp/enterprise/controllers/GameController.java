package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.models.WordleDisplayDetail;
import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.Guess;
import com.quizapp.enterprise.models.game.GuessResult;
import com.quizapp.enterprise.models.game.Player;
import com.quizapp.enterprise.services.IGameService;
import com.quizapp.enterprise.webSockets.PlayerJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    IGameService gameService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;


    @PostMapping(value = "/newGame/{quizId}")
    public Game startNewGame(@PathVariable("quizId") int quizId){
        return gameService.startNewGame(quizId);
    }


    @GetMapping(value = "/{gameId}")
    public Game getGame(@PathVariable("gameId") String gameId){
      return gameService.getGame(gameId);
    }

    @PostMapping(value = "/joinGame/{gameId}")
    public Game joinGame(@PathVariable("gameId") String gameId, @RequestBody Player player) throws Exception {
        gameService.joinGame(gameId, player);
        return gameService.getGame(gameId);
    }

    @PostMapping(value = "/{id}/nextQuestion")
    public Question nextQuestion(@PathVariable("id") String gameId){
        return gameService.nextQuestion(gameId);
    }

    @GetMapping("")
    public ArrayList<Game> getAllGames(){
        return gameService.getAllGames();
    }

    @PostMapping("/checkGuess")
    public GuessResult checkGuess(@RequestBody Guess userGuess){
        GuessResult result = gameService.GetGuessResult(userGuess.getGuess(), userGuess.getQuestionId());
        result.setGameId(userGuess.getGameCode());
        result.setPlayerUsername(userGuess.getPlayerName());

        if(result.isWordCorrect()){
            sendGuessResult(result);
        }
        return result;
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

    @MessageMapping("/chat1")
    public void send(WordleDisplayDetail wordleDisplayDetails) throws Exception {
        Question newQuestion  = gameService.nextQuestion(wordleDisplayDetails.getGameId());
        wordleDisplayDetails.setWordleLength(newQuestion.getWordle().length());
        wordleDisplayDetails.setQuestionId(newQuestion.getQuestionId().intValue());
        wordleDisplayDetails.setTotalGuesses(newQuestion.getTotalGuessesAllowed());
        wordleDisplayDetails.setWordleTimeLimit(newQuestion.getQuestionTimeLimitSeconds());
        messagingTemplate.convertAndSend("/game1/newQuestion/"  + wordleDisplayDetails.getGameId(), wordleDisplayDetails);

    }
}
