package com.quizapp.enterprise.persistence;

import com.quizapp.enterprise.models.Quiz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

public interface QuizRepository extends CrudRepository<Quiz, Integer> {

    @Query(value = "SELECT q FROM Quiz q where q.userId  = ?1")
     ArrayList<Quiz> findQuizzesByUser(int user_id);



}
