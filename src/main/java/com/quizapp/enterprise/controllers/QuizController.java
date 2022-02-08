package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.persistence.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Id;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
public class QuizController {

    @RequestMapping("/quizzes")
    public String userQuizes(){
        return "quiz_list";
    }

}
