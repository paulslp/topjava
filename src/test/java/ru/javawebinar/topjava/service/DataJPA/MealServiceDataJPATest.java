package ru.javawebinar.topjava.service.DataJPA;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDataJPATest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() throws Exception {
        Meal actualMeal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        User actualUser = actualMeal.getUser();
        assertMatch(actualMeal, ADMIN_MEAL1);
        assertMatch(actualUser, ADMIN);
    }
}
