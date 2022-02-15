package com.quizapp.enterprise.models.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    private String playerUsername;
    private int totalPoints;
    private boolean isHost;

    //TODO list of available sabotages
}
