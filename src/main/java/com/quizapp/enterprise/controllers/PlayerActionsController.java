package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.models.Sabotage;
import com.quizapp.enterprise.models.SabotageType;
import com.quizapp.enterprise.models.game.Player;
import com.quizapp.enterprise.models.game.PlayerRound;
import com.quizapp.enterprise.services.GameService;
import com.quizapp.enterprise.services.ISabotageService;
import com.quizapp.enterprise.services.SabotageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sabotage")
public class PlayerActionsController {

    @Autowired
    private GameService gameService;

    @Autowired
    private ISabotageService sabotageService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @PostMapping("/{playerName}/{gameId}")
    public void useSabotage(@PathVariable("playerName") String playerName, @PathVariable("gameId") String gameId, @RequestParam("saboteur") String saboteur){
        sendSabotage(playerName, saboteur, gameId);
    }

    @MessageMapping("/playerSabotage")
    private void sendSabotage(String playerName, String saboteur, String gameId){

        SabotageType sabotageType = sabotageService.generateRandomSabotage();
        String destination = "/game1/" + gameId + "/" + playerName + "/sabotage/";
        messagingTemplate.convertAndSend(destination, new Sabotage(playerName, saboteur, sabotageType));
    }



}
