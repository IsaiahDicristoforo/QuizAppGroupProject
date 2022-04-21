package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.Quiz;

import java.util.ArrayList;

public interface IQuizService {

    Quiz createQuiz(Quiz quizToAdd, String username);
    Quiz getQuiz(int quizId) throws Exception;
    void deleteQuiz(int quizId);

    ArrayList<Quiz> getAllQuizzes(String username);
}
