package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.models.Quiz;
import com.quizapp.enterprise.services.IQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class QuizController {

    @Autowired
    private IQuizService quizService;


    @RequestMapping("/quizzes")
    public String userQuizzes(Model model, Principal principal){


        model.addAttribute("username",principal.getName());
        model.addAttribute("quizzes", quizService.getAllQuizzes(principal.getName()));
      
        return "quiz_list";
    }

    @RequestMapping("/quizCreation")
    public String displayNewQuizPage(){
        return "quizCreation";
    }

    @PostMapping("/quiz")
    @ResponseBody
    public Quiz createNewQuiz(@RequestBody Quiz quizToAdd, Authentication authenticationDetails){
        return quizService.createQuiz(quizToAdd, authenticationDetails.getName());
    }

    @GetMapping("/quiz/{id}")
    @ResponseBody
    public Quiz getQuizById(@PathVariable("id") int id) throws Exception {
        return quizService.getQuiz(id);
    }

    @DeleteMapping("/quiz/{id}")
    @ResponseBody
    public void deleteQuizById(@PathVariable("id") int id){
         quizService.deleteQuiz(id);
    }

}
