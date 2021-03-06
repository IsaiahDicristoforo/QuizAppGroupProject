package com.quizapp.enterprise.services;

import com.quizapp.enterprise.models.SabotageType;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SabotageService implements ISabotageService {
    @Override
    public SabotageType generateRandomSabotage() {
        Random r = new Random();
        return SabotageType.values()[r.nextInt(SabotageType.values().length)];
    }
}
