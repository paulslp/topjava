package ru.javawebinar.topjava.web.meal;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractTestWithAuthUserRestore;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.web.json.JsonUtil.writeValue;
import static ru.javawebinar.topjava.web.meal.MealRestController.REST_URL;

public class MealRestControllerTest extends AbstractTestWithAuthUserRestore {

    @Autowired
    protected MealService mealService;

    @Test
    void testGet() throws Exception {
        SecurityUtil.setAuthUserId(USER_ID);
        TestUtil.print(
                mockMvc.perform(get(REST_URL + "/" + MEAL1_ID))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(contentJson(MEAL1))
        );
    }

    @Test
    void testDelete() throws Exception {
        SecurityUtil.setAuthUserId(ADMIN_ID);
        mockMvc.perform(delete(REST_URL + "/" + ADMIN_MEAL_ID))
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(ADMIN_ID), Arrays.asList(ADMIN_MEAL2));
    }

    @Test
    void testUpdate() throws Exception {
        SecurityUtil.setAuthUserId(USER_ID);
        Meal updated = new Meal(LocalDateTime.now(), "new description", 300);
        updated.setId(MEAL1_ID);
        updated.setUser(USER);
        mockMvc.perform(put(REST_URL + "/" + MEAL1_ID, updated).contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(new Meal(mealService.get(MEAL1_ID, USER_ID)), updated);
    }

    @Test
    void testCreate() throws Exception {
        SecurityUtil.setAuthUserId(USER_ID);
        Meal created = new Meal(LocalDateTime.now(), "new description", 300);
        created.setUser(USER);
        mockMvc.perform(post(REST_URL, created).contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(created)))
                .andDo(print());
        created.setId(100010);
        assertMatch(new Meal(mealService.get(100010, USER_ID)), created);
    }

    @Test
    void testGetAll() throws Exception {
        SecurityUtil.setAuthUserId(USER_ID);
        TestUtil.print(
                mockMvc.perform(get(REST_URL))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(content().json(writeValue(MealsUtil.getWithExcess(Arrays.asList(MEAL1, MEAL2, MEAL3, MEAL4, MEAL5, MEAL6), DEFAULT_CALORIES_PER_DAY)))
                        ));
    }

    @Test
    void testGetBetweenOptional() throws Exception {
        testGetBetween("/filter?startDate=2015-05-30&startTime=00:00&endDate=2015-05-30&endTime=23:10");
    }

    @Test
    void testGetBetweenOptionalWithNull() throws Exception {
        testGetBetween("/filter?startDate=2015-05-30&endDate=2015-05-30");
    }

    void testGetBetween(String query) throws Exception {
        SecurityUtil.setAuthUserId(USER_ID);
        TestUtil.print(
                mockMvc.perform(get(REST_URL + query))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(content().json(writeValue(MealsUtil.getWithExcess(Arrays.asList(MEAL1, MEAL2, MEAL3), DEFAULT_CALORIES_PER_DAY)))
                        ));
    }

}
