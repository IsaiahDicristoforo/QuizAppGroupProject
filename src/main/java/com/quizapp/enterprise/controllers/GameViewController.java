package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.errorHandling.BusinessLogicError;
import com.quizapp.enterprise.services.GameService;
import com.quizapp.enterprise.services.IGameService;
import com.quizapp.enterprise.services.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameViewController {

    @Autowired
    private IGameService gameService;


    @RequestMapping("game/")
    public String game(@RequestParam("gameId") String gameId, Model model) throws BusinessLogicError {

        model.addAttribute("players", gameService.getGame(gameId).getPlayers());
        model.addAttribute("gameCode", gameId);

        return "game";
    }

    @GetMapping("gameView/host/{gameId}")
    public String hostGame(@PathVariable("gameId") String gameId, Model model) throws BusinessLogicError {
        model.addAttribute("gameCode", gameId);
        model.addAttribute("totalQuestions", gameService.getGame(gameId).getQuestions().size());
        return "hostView";
    }


    }




