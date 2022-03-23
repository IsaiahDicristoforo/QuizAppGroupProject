package com.quizapp.enterprise.persistence;

import com.quizapp.enterprise.events.RoundOverEvent;
import com.quizapp.enterprise.events.RoundOverEventPublisher;
import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.GameStatus;
import com.quizapp.enterprise.models.game.Player;
import com.quizapp.enterprise.models.game.PlayerRound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        return games.stream()
                .filter(g -> g.getGameCode().equals(gameCode))
                .findAny()
                .get();
    }

    public void joinGame(
            String gameCode, Player playerToJoin)  {
        getGameByCode(gameCode)
                .getPlayers()
                .add(playerToJoin);
    }

    public Question getNextQuestion(String gameCode) throws Exception {

        Game game = getGameByCode(gameCode);
      if(game == null){
          throw new Exception("game cannot be null");
      }

        if(!(game.getCurrentQuestionNumber() == game.getQuestions().size())){
            game.getPlayers().forEach(x -> x.setRound(new PlayerRound()));

            Question newQuestion = null;
            if(game.getCurrentQuestionNumber() == 0){
                newQuestion = game.getQuestions().get(0);
            }else{
                newQuestion = game.getQuestions().get(game.getCurrentQuestionNumber());
            }

            game.setCurrentQuestionNumber(game.getCurrentQuestionNumber() + 1);
            game.setGameStatus(GameStatus.Started);
            return newQuestion;
        }else{
            return null;
        }


    }

    public void updatePlayerRound(String gameCode, String playerName, boolean correct, boolean setComplete){
        Game game = getGameByCode(gameCode);
        Player playerToUpdate =  game.getPlayers().stream().filter(player -> player.getPlayerUsername().equals(playerName)).findFirst().get();
        playerToUpdate.getRound().setComplete(setComplete);
        updateGameState(gameCode);
    }


    private void updateGameState(String gameCode){

        Game game = getGameByCode(gameCode);
        if(game.getPlayers().stream().allMatch(player -> player.getRound().isComplete())){
            game.setGameStatus(GameStatus.RoundEnded);
            if(game.getCurrentQuestionNumber() == game.getQuestions().size()){
                game.setGameStatus(GameStatus.GameEnded);
            }
        }

    }

    public void updateGameState(GameStatus gameStatus, String gameId){
        getGameByCode(gameId).setGameStatus(gameStatus);
    }

    public Player getPlayer(String gameCode, String username){
        return getGameByCode(gameCode).getPlayers().stream().filter(p -> p.getPlayerUsername().equals(username)).findFirst().get();
    }

    public ArrayList<Player> getLeaderboard(String gameId){
        ArrayList<Player> players = getGameByCode(gameId).getPlayers();
         Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if(o1.getTotalPoints() > o2.getTotalPoints()){
                    return -1;
                }else {
                    return 1;
                }
            }
        });

         return players;
    }

    public static synchronized GameTracker getInstance() {
        gameTracker = (gameTracker != null) ? gameTracker : new GameTracker();
        return gameTracker;
    }

}
