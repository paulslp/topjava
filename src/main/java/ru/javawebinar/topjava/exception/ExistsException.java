package ru.javawebinar.topjava.exception;

public class ExistsException extends MealException {

    public ExistsException(String uuid) {
        super("Meal with " + uuid + " already exist");
    }
}
