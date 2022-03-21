package com.quizapp.enterprise.persistence;

import com.quizapp.enterprise.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {


    List<Question> findByquizId(Integer quizId);



}
