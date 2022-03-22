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
        StringBuilder roundOverCodeString = new StringBuilder("/game1/roundOver/")
                .append(event.gameCode);
        messagingTemplate.convertAndSend(roundOverCodeString.toString(), event.leaderboard);

    }
}
