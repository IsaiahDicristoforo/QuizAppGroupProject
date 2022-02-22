package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.GameStatus;
import com.quizapp.enterprise.models.game.Player;
import com.quizapp.enterprise.persistence.GameTracker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class GameService implements IGameService {
    @Override
    public Game startNewGame(int quizId) {

        Game newGame = new Game();
        newGame.setGameCode(UUID.randomUUID().toString().substring(0, 5));
        newGame.setGameStatus(GameStatus.Started);
        newGame.setQuizId(quizId);

        GameTracker.getInstance().addGame(newGame);

        return newGame;

    }

    @Override
    public ArrayList<Game> getAllGames() {
        return GameTracker.getInstance().getAllGames();
    }

    @Override
    public Game getGame(String gameId) {
        return GameTracker.getInstance().getGameByCode(gameId);
    }

    @Override
    public void joinGame(String gameId, Player playerToJoin) throws Exception {

        if (userNameExists(playerToJoin.getPlayerUsername(), gameId)) {
            throw new Exception("Username already exists. Please choose another username");
        }
        GameTracker.getInstance().joinGame(gameId, playerToJoin);
    }

    private boolean userNameExists(String userName, String gameCode) {
        return GameTracker.getInstance().getGameByCode(gameCode).getPlayers().stream().anyMatch(player -> player.getPlayerUsername().equals(userName));
    }
}
