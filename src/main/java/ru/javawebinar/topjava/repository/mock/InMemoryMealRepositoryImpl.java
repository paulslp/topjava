package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.getUserId()));
    }


    @Override
    public Meal save(Meal meal, int authUserId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            log.info("saveCreate{} " + meal.toString());
            return meal;
        }
        if (get(meal.getId(), authUserId) != null) {
            repository.put(meal.getId(), meal);
            log.info("saveUpdate{} " + meal.toString());
            return meal;
        }
        return null;
    }

    @Override
    public Boolean delete(int id, int authUserId) {
        if (get(id, authUserId) == null) {
            log.info("delete{} false");
            return false;
        } else {
            repository.remove(id);
            log.info("delete{} true");
            return true;
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
    public List<Meal> getAllWithFilter(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        List<Meal> mealList = repository.values().stream()
                .filter(meal -> ((DateTimeUtil.isBetween(meal.getDate(), startDate, endDate)) &&
                        (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) &&
                        (meal.getUserId() == authUserId())))
                .collect(Collectors.toList());
        mealList.sort(((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime())));

        log.debug("getAllWithFilter{} ");
        mealList.forEach(meal -> log.info(meal.toString()));

        return mealList;
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> mealList = new ArrayList<>(repository.values());
        mealList.sort(((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime())));

        log.info("getAll{} ");
        mealList.forEach(meal -> log.info(meal.toString()));

        return mealList;
    }
}

