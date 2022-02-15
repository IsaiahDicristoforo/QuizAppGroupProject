package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.webSockets.PlayerJoinEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameEventController {

    @MessageMapping("/chat")
    @SendTo("/game1/messages")
    public PlayerJoinEvent send(PlayerJoinEvent message) throws Exception {
        return message;
    }
}
