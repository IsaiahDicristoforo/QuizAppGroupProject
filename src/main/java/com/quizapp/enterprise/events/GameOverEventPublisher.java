package com.quizapp.enterprise.events;

import com.quizapp.enterprise.models.game.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class GameOverEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public void publishGameOverEvent(String gameCode, ArrayList<Player> leaderboard) {
        GameOverEvent gameOverEvent = new GameOverEvent(this, gameCode, leaderboard);
        applicationEventPublisher.publishEvent(gameOverEvent);
    }
}
