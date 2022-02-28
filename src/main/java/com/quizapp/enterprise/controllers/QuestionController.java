package com.quizapp.enterprise.controllers;

import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.persistence.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/questions")
    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }

    @PostMapping("/questions")
    public ResponseEntity<String> addQuestion(@Valid @RequestBody Question questionToAdd){
        try {
            questionRepository.save(questionToAdd);
            return ResponseEntity.ok("Valid Question");
        }
        catch {
            return ResponseEntity.bad("Invalid Question");
        }

    }

}
