package com.quizapp.enterprise.models;

import com.quizapp.enterprise.models.game.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sabotage {
    private String playerBeingSabotaged;
    private String saboteur;
    private SabotageType sabotageType;
}
