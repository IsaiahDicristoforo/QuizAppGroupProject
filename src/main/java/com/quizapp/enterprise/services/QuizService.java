package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.Quiz;
import com.quizapp.enterprise.models.User;
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
    public Quiz createQuiz(Quiz quizToAdd, String username) throws Exception {

       User user = userRepository.findUserByEmail(username);
       if(user == null){
           throw new Exception("user not found");
       }
       quizToAdd.setUserId(user.getUserID());

       return quizRepository.save(quizToAdd);
    }

    @Override
    public Quiz getQuiz(int quizId) throws Exception {

        Optional<Quiz> quizToReturn =  quizRepository.findById(quizId);
        if(!quizToReturn.isPresent()){
            StringBuilder missingQuizException = new StringBuilder("Quiz with id ")
                    .append(quizId)
                    .append("does not exist");
            throw new Exception(missingQuizException.toString());
        }else{
            return quizToReturn.get();
        }

    }

    @Override
    public ArrayList<Quiz> getAllQuizzes(String username) throws Exception {
        User user = userRepository.findUserByEmail(username);
        if(user == null){
            throw new Exception("user not found");
        }

        return quizRepository.findQuizzesByUser(user.getUserID());

    }






}
