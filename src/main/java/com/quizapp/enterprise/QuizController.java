package com.quizapp.enterprise;

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

    @Autowired
    private QuestionRepository questionRepository;

    @RequestMapping("/start")
    public String index(){
        return "start";
    }

    @GetMapping(value = "/questions", produces = "application/json")
   public Question GetAllQuestions(){


        List<Question> q = questionRepository.findAll();

        return (questionRepository.findAll().get(0));
    }

}
