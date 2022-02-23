package com.quizapp.enterprise.persistence;

import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.Player;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class GameTracker {

    private static GameTracker gameTracker;

    private static ArrayList<Game> games;


    private GameTracker(){
        games = new ArrayList<Game>();
    }

    public void addGame(Game game){
        games.add(game);
    }

    public ArrayList<Game> getAllGames(){
        return games;
    }

    public Game getGameByCode(String gameCode){
        return games.stream().filter(g -> g.getGameCode().equals(gameCode)).findAny().get();
    }

    public void joinGame(String gameCode, Player playerToJoin){
        getGameByCode(gameCode).getPlayers().add(playerToJoin);
    }

    public static synchronized GameTracker getInstance() {
        gameTracker = (gameTracker != null) ? gameTracker : new GameTracker();
        return gameTracker;
    }

}
