package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.getWithExceeded;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());


    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealWithExceed> getAll() {
        log.info("getAll");

        return getWithExceeded(service.getAll(meal -> meal.getUserId() == authUserId()), authUserCaloriesPerDay());
    }


    public List<MealWithExceed> getAll(String startDate, String endDate, String startTime, String endTime) {
        log.info("getAllWithFilter");
        LocalDate startD = startDate.equals("") ? LocalDate.MIN : LocalDate.parse(startDate);
        LocalDate endD = endDate.equals("") ? LocalDate.MAX : LocalDate.parse(endDate);
        LocalTime startT = startTime.equals("") ? LocalTime.MIN : LocalTime.parse(startTime);
        LocalTime endT = endTime.equals("") ? LocalTime.MAX : LocalTime.parse(endTime);

        return getWithExceeded(service.getAll(meal -> ((DateTimeUtil.isBetween(meal.getDate(), startD, endD)) &&
                (DateTimeUtil.isBetween(meal.getTime(), startT, endT)) &&
                (meal.getUserId() == authUserId()))), authUserCaloriesPerDay());
    }


    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        meal.setUserId(authUserId());
        return service.create(meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        meal.setUserId(authUserId());
        service.update(meal);
    }


}