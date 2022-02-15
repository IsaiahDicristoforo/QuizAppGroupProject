package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.GameStatus;
import com.quizapp.enterprise.models.game.Player;
import com.quizapp.enterprise.persistence.GameTracker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class GameService implements IGameService{
    @Override
    public Game startNewGame(int quizId) {

        Game newGame = new Game();
        newGame.setGameCode(UUID.randomUUID().toString().substring(0,5));
        newGame.setGameStatus(GameStatus.Started);
        newGame.setQuizId(quizId);

        GameTracker.getInstance().addGame(newGame);

        return newGame;

    }

    @Override
    public ArrayList<Game> getAllGames() {
        return null;
    }

    @Override
    public Game getGame(String gameId) {
        return GameTracker.getInstance().getGameByCode(gameId);
    }

    @Override
    public void joinGame(int gameId, Player playerToJoin) {

    }
}
