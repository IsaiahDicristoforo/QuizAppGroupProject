package com.quizapp.enterprise.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "hints")
@Data
public class Hint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hint_id")
    private int hintId;

    private String hint;
}
