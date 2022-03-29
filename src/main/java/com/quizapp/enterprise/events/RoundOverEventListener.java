package com.quizapp.enterprise.events;

import com.quizapp.enterprise.models.game.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RoundOverEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    @MessageMapping("/chat1")
    public void onApplicationEvent(RoundOverEvent event) {
        messagingTemplate.convertAndSend("/game1/roundOver/" + event.gameCode , event.leaderboard);

    }
}
