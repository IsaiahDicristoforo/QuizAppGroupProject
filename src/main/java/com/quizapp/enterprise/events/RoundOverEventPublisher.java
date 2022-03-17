package com.quizapp.enterprise.events;

import com.quizapp.enterprise.models.game.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Component
public class RoundOverEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishRoundOverEvent(String gameCode, ArrayList<Player> leaderboard) {
        RoundOverEvent roundOverEvent = new RoundOverEvent(this, gameCode, leaderboard);
        applicationEventPublisher.publishEvent(roundOverEvent);
    }
}
