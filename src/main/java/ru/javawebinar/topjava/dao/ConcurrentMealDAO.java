package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ConcurrentMealDAO extends MealDao {

    private static ConcurrentMap<String, Meal> concurrentMealMap = new ConcurrentHashMap<>();


    @Override
    public Meal getById(String mealId) {
        return concurrentMealMap.get(mealId);
    }

    @Override
    public void insert(Meal meal) {
        concurrentMealMap.put(meal.getId(), meal);
    }

    @Override
    protected void doDelete(String mealId) {
        concurrentMealMap.remove(mealId);
    }

    @Override
    protected void doUpdate(Meal meal) {
        concurrentMealMap.put(meal.getId(), meal);
    }

    @Override
    protected List<Meal> doCopyAll() {
        return new ArrayList<>(concurrentMealMap.values());
    }

    @Override
    public Boolean isExists(String mealId) {
        return concurrentMealMap.containsKey(mealId);
    }
}
