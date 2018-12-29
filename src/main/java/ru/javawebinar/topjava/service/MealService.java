package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.function.Predicate;

public interface MealService {
    void delete(int id, int authUserId);

    List<Meal> getAll(Predicate<Meal> filter);

    Meal get(int id, int authUserId);

    Meal create(Meal meal);

    void update(Meal meal);
}