package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.Quiz;

public interface IQuizService {

    Quiz createQuiz(Quiz quizToAdd, String username);
}
