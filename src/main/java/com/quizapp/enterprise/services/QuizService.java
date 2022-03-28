package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.Quiz;
import com.quizapp.enterprise.persistence.QuizRepository;
import com.quizapp.enterprise.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class QuizService implements IQuizService {

        @Autowired
        private QuizRepository quizRepository;

        @Autowired
        private UserRepository userRepository;


    @Override
    public Quiz createQuiz(Quiz quizToAdd, String username) {

       int userId =  userRepository.findUserByUsername(username).getUserID();

       quizToAdd.setUserId(userId);

       return quizRepository.save(quizToAdd);
    }

    @Override
    public Quiz getQuiz(int quizId) throws Exception {

        Optional<Quiz> quizToReturn =  quizRepository.findById(quizId);
        if(quizToReturn.isEmpty()){
            throw new Exception("Quiz with id "  + quizId +  "does not exist");

        }else{
            return quizToReturn.get();
        }

    }

    @Override
    public ArrayList<Quiz> getAllQuizzes(String username){
        int userId =  userRepository.findUserByUsername(username).getUserID();

        return quizRepository.findQuizzesByUser(userId);

    }





}
