package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ConcurrentMealDAO implements MealDao {

    public ConcurrentMealDAO() {
        super();
        this.id = new AtomicInteger(1);
    }

    private ConcurrentMap<Integer, Meal> concurrentMealMap = new ConcurrentHashMap<>();

    private AtomicInteger id;

    @Override
    public Meal read(int mealId) {
        return concurrentMealMap.get(mealId);
    }

    @Override
    public Meal add(Meal meal) {
        int mealId = id.incrementAndGet();
        Meal mealWithId = new Meal(mealId, meal.getDateTime(), meal.getDescription(), meal.getCalories());
        concurrentMealMap.putIfAbsent(mealId, mealWithId);
        return mealWithId;
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
        List<Meal> mealList = new ArrayList<>(concurrentMealMap.values());
        mealList.sort((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
        return mealList;
    }


}
