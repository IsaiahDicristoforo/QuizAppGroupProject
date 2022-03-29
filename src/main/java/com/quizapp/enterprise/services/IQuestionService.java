package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.Question;
import java.util.ArrayList;

public interface IQuestionService {

    Question addQuestion(Question question);
    ArrayList<Question> addQuestions(ArrayList<Question> questions);
    Question getQuestion(int questionId) throws Exception;
    ArrayList<Question> getAllQuestions(int quizId);
    ArrayList<Question> getAllQuestions();
}
