package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.Quiz;

import java.util.ArrayList;

public interface IQuizService {

    Quiz createQuiz(Quiz quizToAdd, String username) throws Exception;
    Quiz getQuiz(int quizId) throws Exception;

    ArrayList<Quiz> getAllQuizzes(String username) throws Exception;
}
