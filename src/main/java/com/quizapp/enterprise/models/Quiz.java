package com.quizapp.enterprise.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "quizId")
    private int quizId;


}
