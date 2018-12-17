package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.exception.ExistsException;
import ru.javawebinar.topjava.exception.NotExistsException;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class MealDao {

    private static final Logger log = getLogger(MealDao.class);

    public abstract Meal getById(String mealId);

    public abstract void insert(Meal meal);

    protected abstract void doDelete(String mealId);

    protected abstract void doUpdate(Meal meal);

    protected abstract List<Meal> doCopyAll();

    public abstract Boolean isExists(String mealId);

    public void add(Meal meal) {
        if (!isExists(meal.getId())) {
            insert(meal);
        } else {
            throw new ExistsException(meal.getId());
        }
    }


    public void delete(String mealId) {
        if (isExists(mealId)) {
            doDelete(mealId);
        } else {
            throw new ExistsException(mealId);
        }

    }


    public void update(Meal meal) {
        if (isExists(meal.getId())) {
            doUpdate(meal);
        } else {
            throw new NotExistsException(meal.getId());
        }

    }

    public List<Meal> getAllSorted() {
        log.info("getAllSorted");
        List<Meal> list = doCopyAll();
        list.sort((o1, o2) -> (o2.getDateTime().compareTo(o1.getDateTime())));
        return list;
    }


}


