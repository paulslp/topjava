package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {

    Meal add(Meal meal);

    Meal read(int mealId);

    void update(Meal meal);

    void delete(int mealId);

    List<Meal> getAll();

}


