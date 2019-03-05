package ru.javawebinar.topjava.service.DataJPA;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.ArrayList;
import java.util.Arrays;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)

public class UserServiceDataJPATest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() throws Exception {
        ADMIN.setMeals(Arrays.asList(ADMIN_MEAL2, ADMIN_MEAL1));
        User actual = service.getWithMeals(ADMIN_ID);
        assertMatch(actual, ADMIN);
        assertMatch(actual.getMeals(), ADMIN.getMeals());
    }

    @Test
    public void getWithOutMeals() throws Exception {
        User user_Without_Meals = new User(ADMIN_MEAL_ID + 2, "User_Without_Meals", "user_w_m@yandex.ru", "password", Role.ROLE_USER);
        user_Without_Meals.setMeals(new ArrayList<>());
        service.create(user_Without_Meals);
        User user_from_db = service.getWithMeals(ADMIN_MEAL_ID + 2);
        assertMatch(user_from_db, user_Without_Meals);
        assertMatch(user_from_db.getMeals(), user_Without_Meals.getMeals());
    }


}
