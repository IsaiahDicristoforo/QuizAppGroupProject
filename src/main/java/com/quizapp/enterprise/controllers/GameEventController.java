package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.models.game.Guess;
import com.quizapp.enterprise.webSockets.PlayerJoinEvent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class GameEventController {

    public ArrayList<Guess> CheckGuess(String userGuess, String correctAnswer) {
        var userGuessArr = userGuess.toCharArray();
        var correctAnswerArr = correctAnswer.toCharArray();

        var userGuessList = new ArrayList<Guess>();

        for(int i = 0; i <= userGuess.length(); i++) {
            if(userGuessArr[i] == correctAnswerArr[i]){
                userGuessList.add(new Guess(true, Character.toString(userGuessArr[i])));
            } else {
                userGuessList.add(new Guess(false, Character.toString(userGuessArr[i])));
            }
        }

        return userGuessList;
    }
}
