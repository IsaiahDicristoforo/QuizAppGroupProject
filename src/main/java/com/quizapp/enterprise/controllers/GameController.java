package com.quizapp.enterprise.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameController {

    @RequestMapping("game")
    public String game(){
        return "game";

    }
}