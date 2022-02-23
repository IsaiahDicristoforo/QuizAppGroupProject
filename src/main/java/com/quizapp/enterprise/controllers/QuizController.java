package com.quizapp.enterprise.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class QuizController {

    @RequestMapping("/quizzes")
    public String userQuizes(Model model, Principal principal) {

        model.addAttribute("username", principal.getName());
        return "quiz_list";
    }
}
