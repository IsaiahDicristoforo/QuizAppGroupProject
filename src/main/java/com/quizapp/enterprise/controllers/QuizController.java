package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.models.Quiz;
import com.quizapp.enterprise.persistence.QuestionRepository;
import com.quizapp.enterprise.persistence.QuizRepository;
import com.quizapp.enterprise.services.CustomUserDetailsService;
import com.quizapp.enterprise.services.IQuizService;
import com.quizapp.enterprise.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class QuizController {

    @Autowired
    private IQuizService quizService;


    @RequestMapping("/quizzes")
    public String userQuizes(Model model, Principal principal) throws Exception {


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
    public Quiz createNewQuiz(@RequestBody Quiz quizToAdd, Authentication authenticationDetails) throws Exception {
        return quizService.createQuiz(quizToAdd, authenticationDetails.getName());
    }

    @GetMapping("/quiz/{id}")
    @ResponseBody
    public Quiz getQuizById(@PathVariable("id") int id) throws Exception {
        return quizService.getQuiz(id);
    }

}
