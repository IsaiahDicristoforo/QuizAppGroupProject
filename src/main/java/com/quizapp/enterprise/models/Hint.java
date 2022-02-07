package com.quizapp.enterprise.models;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "hints")
@Data
public class Hint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int hintId;

    private String hint;

}
