package ru.javawebinar.topjava.web;

import org.hamcrest.*;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

class RootControllerTest extends AbstractControllerTest {


    @Test
    void testUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(START_SEQ)),
                                hasProperty("name", is(USER.getName()))
                        )
                )));
    }

    @Test
    void testMeals() throws Exception {
        int userId_before = SecurityUtil.authUserId();
        SecurityUtil.setAuthUserId(START_SEQ + 1);
        List<MealTo> expectedList = MealsUtil.getWithExcess(Arrays.asList(ADMIN_MEAL2, ADMIN_MEAL1), SecurityUtil.authUserCaloriesPerDay());
        mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(2)))
                .andExpect(model().attribute("meals", mealToMatcher(expectedList, "CompareMealToLists")));
        SecurityUtil.setAuthUserId(userId_before);
    }


    private Matcher<List<MealTo>> mealToMatcher(final List<MealTo> expectedList, String description) {
        return new CustomMatcher<>(description) {
            @Override
            public boolean matches(Object actualList) {
                assertMatch((List<MealTo>) actualList, expectedList);
                return true;
            }
        };
    }



}