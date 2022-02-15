package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameManagementController {

    @Autowired
    IGameService gameService;

    // start game -  start a game by selecting a quiz. The person who starts the game is the host.

    // join game - join game by the game id

    @PostMapping(value = "/newGame/{quizId}")
    public Game startNewGame(@PathVariable("quizId") int quizId){
        return gameService.startNewGame(quizId);
    }


    @GetMapping(value = "/{gameId}")
    public Game getGame(@PathVariable("gameId") String gameId){
      return gameService.getGame(gameId);
    }

    //leave game

    //Delete game

    //Get all games


}
