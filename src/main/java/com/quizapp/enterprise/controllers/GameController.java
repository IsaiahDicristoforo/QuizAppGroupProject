package com.quizapp.enterprise.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameController {

    @RequestMapping("gameView/{gameId}")
    public String game(@PathVariable("gameId") String gameId){
        return "game";
    }


}
