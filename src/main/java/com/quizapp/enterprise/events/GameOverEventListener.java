package com.quizapp.enterprise.events;

import com.quizapp.enterprise.models.game.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class GameOverEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    @MessageMapping("/chat")
    public void onApplicationEvent(GameOverEvent event) {
        messagingTemplate.convertAndSend("/game1/gameOver/" + event.gameCode, event.leaderboard);
    }
}
