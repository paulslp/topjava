package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.user.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Test
    void testGet() throws Exception {
        TestUtil.print(
                    mockMvc.perform(get(REST_URL)
                                .with(userHttpBasic(USER)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(getUserMatcher(USER))
        );
    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                    .andExpect(status().isUnauthorized());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL)
                    .with(userHttpBasic(USER)))
                    .andExpect(status().isNoContent());
        assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    void testRegister() throws Exception {
        UserTo createdTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);

        ResultActions action = mockMvc.perform(post(REST_URL + "/register").contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(createdTo)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        User returned = readFromJsonResultActions(action, User.class);

        User created = UserUtil.createNewFromTo(createdTo);
        created.setId(returned.getId());

        assertMatch(returned, created);
        assertMatch(userService.getByEmail("newemail@ya.ru"), created);
    }

    @Test
    void testUpdate() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);

        mockMvc.perform(put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(USER))
                    .content(JsonUtil.writeValue(updatedTo)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

        assertMatch(userService.getByEmail("newemail@ya.ru"), UserUtil.updateFromTo(new User(USER), updatedTo));
    }

    @Test
    void testUpdateNotValid() throws Exception {
        UserTo expected = new UserTo(null, "New", null, "newPass", 2300);
        mockMvc.perform(put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(USER))
                    .content(JsonUtil.writeValue(expected)))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string(containsString("VALIDATION_ERROR")))
                    .andDo(print());
    }


    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testUpdateEmailExists() throws Exception {
        UserTo updated = new UserTo(ADMIN.getId(), ADMIN.getName(), ADMIN.getEmail(), ADMIN.getPassword(), ADMIN.getCaloriesPerDay());
        updated.setEmail("user@yandex.ru");
        mockMvc.perform(put(REST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(userHttpBasic(ADMIN))
                    .content(JsonUtil.writeAdditionProps(updated, "password", ADMIN.getPassword())))
                    .andExpect(status().isConflict())
                    .andExpect(content().string(containsString("DATA_ERROR")))
                    .andExpect(content().string(containsString(messageSource.getMessage("user.email.exists.error", null
                                , LocaleContextHolder.getLocale()
                    ))))
                    .andDo(print());
    }
}