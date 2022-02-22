package com.quizapp.enterprise;

import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.GameStatus;
import com.quizapp.enterprise.services.IGameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EnterpriseApplicationTests {
    @Autowired
    IGameService gameService;

    @Test
    void contextLoads() {
    }

    /**
     * Validate that the DTO properties can be set and retrieved. (Based on other unit tests in class)
     * @author Mahesh Gowda
     */
    @Test
    void verifyGameProperties() {
        String gameCode =  "TEST";
        GameStatus gameStatus = GameStatus.valueOf("NotStarted");

        Game game = new Game();
        game.setGameCode(gameCode);
        game.setGameStatus(gameStatus);
        assertEquals(gameCode, game.getGameCode());
        assertEquals(gameStatus, game.getGameStatus());
    }

    /**
     * Validate that the GameService can save and return games. (based on individual assignment)
     * @author Mahesh Gowda
     */
    @Test
    void verifyAddAndRemoveGames() {
        String gameCode =  "FIRST";

        Game game = new Game();
        game.setGameCode(gameCode);

        List<Game> games = gameService.getAllGames();
        boolean gamePresent = false;
        for (Game g : games) {
            if (g.getGameCode().equals(gameCode)) {
                gamePresent = true;
                break;
            }
        }

        assertTrue(gamePresent);
    }

}
