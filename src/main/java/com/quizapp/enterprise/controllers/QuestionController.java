package com.quizapp.enterprise.controllers;
import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.services.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    @GetMapping("/questions")
    public List<Question> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @PostMapping("/question")
    public ResponseEntity<String> addQuestion(@Valid @RequestBody Question questionToAdd){
        questionService.addQuestion(questionToAdd);
        return ResponseEntity.ok("Valid Question");
    }

    @PostMapping("/questions")
    public ResponseEntity<String> addQuestion(@Valid @RequestBody List<Question> questionsToAdd){

        questionService.addQuestions((ArrayList<Question>) questionsToAdd);
        return ResponseEntity.ok("List of questions has been saved");
    }

    @GetMapping("/questions/{id}")
    public List<Question> getAllQuestionsByQuizId(@PathVariable("id") int quizId){
        return questionService.getAllQuestions(quizId);
    }

}
