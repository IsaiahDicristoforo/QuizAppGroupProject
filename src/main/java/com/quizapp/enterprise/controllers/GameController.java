package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.models.WordleDisplayDetail;
import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.Player;
import com.quizapp.enterprise.services.IGameService;
import com.quizapp.enterprise.webSockets.PlayerJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    IGameService gameService;


    @PostMapping(value = "/newGame/{quizId}")
    public Game startNewGame(@PathVariable("quizId") int quizId){
        return gameService.startNewGame(quizId);
    }


    @GetMapping(value = "/{gameId}")
    public Game getGame(@PathVariable("gameId") String gameId){
      return gameService.getGame(gameId);
    }

    @PostMapping(value = "/joinGame/{gameId}")
    public Game joinGame(@PathVariable("gameId") String gameId, @RequestBody Player player) throws Exception{
        gameService.joinGame(gameId, player);
        return gameService.getGame(gameId);
    }


    @GetMapping("")
    public ArrayList<Game> getAllGames(){
        return gameService.getAllGames();
    }

    @MessageMapping("/chat")
    @SendTo("/game1/messages")
    public PlayerJoinEvent send(PlayerJoinEvent message) throws Exception {
        Player newPlayer = new Player();
        newPlayer.setTotalPoints(0);
        newPlayer.setPlayerUsername(message.getPlayerName());
        newPlayer.setHost(false);
        gameService.joinGame(message.getGameId(), newPlayer);
        return message;
    }

    @MessageMapping("/chat1")
    @SendTo("/game1/newQuestion")
    public WordleDisplayDetail send(WordleDisplayDetail wordleDisplayDetails) throws Exception {
        return wordleDisplayDetails;
    }
}
