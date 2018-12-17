package ru.javawebinar.topjava.exception;

public class NotExistsException extends MealException {

    public NotExistsException(String uuid) {
        super("Resume " + uuid + " not exist");
    }
}
