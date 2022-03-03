package com.quizapp.enterprise.webSockets;

import lombok.Data;

@Data
public class PlayerJoinEvent {

    private String playerName;

    private String gameId;
}
