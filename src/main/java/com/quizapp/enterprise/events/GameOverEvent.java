package com.quizapp.enterprise.events;

import com.quizapp.enterprise.models.game.Player;
import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;

public class GameOverEvent extends ApplicationEvent {

    public String gameCode;

    public ArrayList<Player> leaderboard;


    public GameOverEvent(Object source, String gameCode, ArrayList<Player> leaderboard) {
        super(source);
        this.gameCode = gameCode;
        this.leaderboard = leaderboard;
    }

}
