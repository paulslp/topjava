package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal));
    }


    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            log.info("saveCreate{} " + meal.toString());
            return meal;
        }
        Meal value = repository.merge(meal.getId(), meal, (oldValue, newValue) -> (oldValue.getUserId() == newValue.getUserId() ? newValue : oldValue));
        return value;
    }


    @Override
    public boolean delete(int id, int authUserId) {
        Meal result = repository.computeIfPresent(id, (key, value) -> (value.getUserId() == authUserId ? null : value));
        if (result == null) {
            log.info("delete{} true");
            return true;
        } else {
            log.info("delete{} false");
            return false;
        }
    }

    @Override
    public Meal get(int id, int authUserId) {
        Meal meal = repository.get(id);
        if ((meal == null) || (meal.getUserId() == authUserId)) {
            log.info("get{} " + String.valueOf(meal));
            return meal;
        } else {
            log.info("get{} null");
            return null;
        }
    }


    @Override
    public List<Meal> getAll(Predicate<Meal> filter) {
        List<Meal> mealList = repository.values().stream()
                .filter(filter)
                .sorted(((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime())))
                .collect(Collectors.toList());

        log.debug("getAllWithFilter{} ");
        mealList.forEach(meal -> log.info(meal.toString()));

        return mealList;
    }

}

