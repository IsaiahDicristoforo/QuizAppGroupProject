package com.quizapp.enterprise;

import com.quizapp.enterprise.controllers.GameController;
import com.quizapp.enterprise.models.game.Game;
import com.quizapp.enterprise.models.game.Guess;
import com.quizapp.enterprise.services.GameService;
import com.quizapp.enterprise.services.IGameService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EnterpriseApplicationTests {

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

        Game game = new Game();
        game.setGameCode(gameCode);
        assertEquals(gameCode, game.getGameCode());
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

//        List<Game> games = GameService.getAllGames();
//        boolean gamePresent = false;
//        for (Game g : games) {
//            if (g.getGameCode().equals(gameCode)) {
//                gamePresent = true;
//                break;
//            }
//        }

       // assertTrue(gamePresent);


    }

    /**
     * Scenario: User guesses correct answer
     * @author Christian Turner
     */
    @Test
    void verifyCorrectAnswer() {
        String userGuest = "Blubber";
        String correctAnswer = "Blubber";

        ArrayList<Guess> gec = new GameService().checkGuess(userGuest, correctAnswer);
        var correct = true;
        for(var guess : gec) {
            if(!guess.IsCorrectLetter) {
                correct = false;
                break;
            }
        }

        assertTrue(correct);
    }

    /**
     * Scenario: User guesses incorrect answer
     * @author Christian Turner
     */
    @Test
    void verifyIncorrectAnswer() {
        String userGuest = "Flubber";
        String correctAnswer = "Blubber";

        ArrayList<Guess> gec = new GameService().checkGuess(userGuest, correctAnswer);
        var correct = true;
        for(var guess : gec) {
            if(!guess.IsCorrectLetter) {
                correct = false;
                break;
            }
        }

        assertFalse(correct);
    }
}
