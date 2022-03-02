package com.quizapp.enterprise.persistence;

import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.Player;
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

    public Question getNextQuestion(String gameCode){
        Game game = getGameByCode(gameCode);

        Question newQuestion = null;
        if(game.getCurrentQuestionNumber() == 0){
            newQuestion = game.getQuestions().get(0);
        }else{
            newQuestion = game.getQuestions().get(game.getCurrentQuestionNumber());
        }

        game.setCurrentQuestionNumber(game.getCurrentQuestionNumber() + 1);

        return newQuestion;


    }

    public static synchronized GameTracker getInstance() {

        if (gameTracker == null) {
            gameTracker = new GameTracker();
        }
        return gameTracker;
    }

}
