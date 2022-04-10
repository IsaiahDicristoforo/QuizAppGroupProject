package com.quizapp.enterprise.errorHandling;

public class BusinessLogicError extends Exception{
    public BusinessLogicError(String message){
        super(message);
    }
}
