package ru.javawebinar.topjava.exception;

public class MealException extends RuntimeException {

    private String uuid;

    public MealException(String message) {
        this(message,  null);
    }


    public MealException(String message, Exception e) {
        super(message, e);
    }



}
