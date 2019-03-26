package ru.javawebinar.topjava.web.meal;


import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.web.meal.MealRestController.REST_URL;

public class MealRestControllerTest extends AbstractControllerTest {
    @Test
    void testGet() throws Exception {
        int userId_before = SecurityUtil.authUserId();
        SecurityUtil.setAuthUserId(USER_ID);
        TestUtil.print(
                mockMvc.perform(get(REST_URL + "/" + MEAL1_ID))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(contentJson(MEAL1))
        );
        SecurityUtil.setAuthUserId(userId_before);
    }

    @Test
    void testDelete() throws Exception {
        int userId_before = SecurityUtil.authUserId();
        SecurityUtil.setAuthUserId(ADMIN_ID);
        mockMvc.perform(delete(REST_URL + "/" + ADMIN_MEAL_ID))
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(ADMIN_ID), Arrays.asList(ADMIN_MEAL2));
        SecurityUtil.setAuthUserId(userId_before);
    }

    @Test
    void testUpdate() throws Exception {
        int userId_before = SecurityUtil.authUserId();
        SecurityUtil.setAuthUserId(USER_ID);
        Meal updated = new Meal(LocalDateTime.now(), "new description", 300);
        updated.setId(MEAL1_ID);
        updated.setUser(USER);
        mockMvc.perform(put(REST_URL + "/" + MEAL1_ID, updated).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(new Meal(mealService.get(MEAL1_ID, USER_ID)), updated);
        SecurityUtil.setAuthUserId(userId_before);
    }


    @Test
    void testGetBetweenOptional() throws Exception {
        int userId_before = SecurityUtil.authUserId();
        SecurityUtil.setAuthUserId(USER_ID);
        TestUtil.print(
                mockMvc.perform(get(REST_URL + "/filter?startDate=2015-05-30&startTime=00:00&endDate=2015-05-30&endTime=23:10"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(contentJson(MEAL1, MEAL2, MEAL3))
        );
        SecurityUtil.setAuthUserId(userId_before);
    }

    @Test
    void testGetBetweenOptionalWithNull() throws Exception {
        int userId_before = SecurityUtil.authUserId();
        SecurityUtil.setAuthUserId(USER_ID);
        TestUtil.print(
                mockMvc.perform(get(REST_URL + "/filter?startDate=2015-05-30&endDate=2015-05-30"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(contentJson(MEAL1, MEAL2, MEAL3))
        );
        SecurityUtil.setAuthUserId(userId_before);
    }

}
