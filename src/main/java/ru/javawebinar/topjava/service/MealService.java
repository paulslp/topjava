package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService {
    void delete(int id, int authUserId);

    List<Meal> getAll();

    List<Meal> getAllWithFilter(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);

    Meal get(int id, int authUserId);

    Meal create(Meal meal);

    void update(Meal meal, int authUserId);
}