package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    static {
        SLF4JBridgeHandler.install();
    }

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID1, USER_ID);
        assertMatch(meal, MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(MEAL_ID1, ADMIN_ID);
    }


    @Test
    public void delete() {
        service.delete(MEAL_ID1, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL4, MEAL3, MEAL2);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(MEAL_ID5, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> allWithFilter = service.getBetweenDates(LocalDate.of(2019, 1, 5), LocalDate.now(), ADMIN_ID);
        assertMatch(allWithFilter, MEAL8, MEAL7);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> allWithFilter = service.getBetweenDateTimes(LocalDateTime.of(2019, 1, 5, 12, 0), LocalDateTime.now(), ADMIN_ID);
        assertMatch(allWithFilter, MEAL8);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, MEAL8, MEAL7, MEAL6, MEAL5);
    }

    @Test
    public void update() {
        Meal updated = new Meal(MEAL1);
        updated.setDescription("UpdatedName");
        updated.setCalories(330);
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_ID1, USER_ID), updated);

    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        Meal updated = new Meal(MEAL1);
        updated.setDescription("UpdatedName");
        updated.setCalories(330);
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2019, 2, 7, 0, 0), "new", 1500);
        Meal created = service.create(newMeal, 100000);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(100000),newMeal, MEAL4, MEAL3, MEAL2, MEAL1);
    }
}