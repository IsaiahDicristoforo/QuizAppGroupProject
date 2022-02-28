package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.Quiz;
import com.quizapp.enterprise.persistence.QuizRepository;
import com.quizapp.enterprise.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService implements IQuizService {

        @Autowired
        private QuizRepository quizRepository;

        @Autowired
        private UserRepository userRepository;


    @Override
    public Quiz createQuiz(Quiz quizToAdd, String username) {

       int userId =  userRepository.findUserByEmail(username).getUserID();

       quizToAdd.setUserId(userId);

       return quizRepository.save(quizToAdd);
    }



}
