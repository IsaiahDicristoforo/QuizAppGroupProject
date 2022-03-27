package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.Question;
import com.quizapp.enterprise.persistence.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class QuestionService implements IQuestionService{

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public ArrayList<Question> addQuestions(ArrayList<Question> questions) {
        return new ArrayList<Question>(questionRepository.saveAll(questions));
    }

    @Override
    public Question getQuestion(int questionId) throws Exception {
        Optional<Question> questionOptional = questionRepository.findById(Long.valueOf(questionId));

        if(questionOptional.isPresent()){
            return questionOptional.get();
        }else{
            throw new Exception("Question does not exist");
        }

    }

    @Override
    public ArrayList<Question> getAllQuestions(int quizId) {
        return (ArrayList<Question>) questionRepository.findByQuizId(quizId);
    }

    @Override
    public ArrayList<Question> getAllQuestions() {
        return (ArrayList<Question>) questionRepository.findAll();
    }
}
