package com.quizapp.enterprise.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "quiz_id")
    private int quizId;

    @Column(name = "name")
    private String quizName;

    @Column(name = "user_id")
    private int userId;

}
