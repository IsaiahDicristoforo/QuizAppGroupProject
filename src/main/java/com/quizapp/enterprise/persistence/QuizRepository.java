package com.quizapp.enterprise.persistence;

import com.quizapp.enterprise.models.Quiz;
import org.springframework.data.repository.CrudRepository;

public interface QuizRepository extends CrudRepository<Quiz, Integer> {

}
