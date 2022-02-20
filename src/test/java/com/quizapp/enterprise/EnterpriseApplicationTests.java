package com.quizapp.enterprise;

import com.quizapp.enterprise.models.game.Game;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EnterpriseApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * Validate that the DTO properties can be set and retrieved. (Based on other unit tests in class)
     */
    @Test
    void verifyGameProperties() {
        String gameCode =  "TEST";

        Game game = new Game();
        game.setGameCode(gameCode);
        assertEquals(gameCode, game.getGameCode());
    }
}
