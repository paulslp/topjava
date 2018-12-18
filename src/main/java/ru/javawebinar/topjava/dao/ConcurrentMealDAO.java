package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ConcurrentMealDAO implements MealDao {

    public ConcurrentMealDAO(AtomicInteger id) {
        super();
        this.id = id;
    }

    private ConcurrentMap<Integer, Meal> concurrentMealMap = new ConcurrentHashMap<>();

    private AtomicInteger id;

    @Override
    public Meal read(int mealId) {
        return concurrentMealMap.get(mealId);
    }

    @Override
    public void add(Meal meal) {
        int mealId = id.incrementAndGet();
        concurrentMealMap.putIfAbsent(mealId, new Meal(mealId, meal.getDateTime(), meal.getDescription(), meal.getCalories()));
    }

    @Override
    public void delete(int mealId) {
        concurrentMealMap.remove(mealId);
    }

    @Override
    public void update(Meal meal) {
        concurrentMealMap.replace(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(concurrentMealMap.values());
    }


}
