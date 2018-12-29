package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UsersUtil {
    public static final List<User> USERS = Arrays.asList(
            new User("igor", "igor@gmail.com", "password", Role.ROLE_USER, null),
            new User("jhon", "jhon@gmail.com", "password", Role.ROLE_USER, null),
            new User("igor", "igor@yandex.ru", "password", Role.ROLE_USER, null),
            new User("admin", "admin@yandex.com", "password", Role.ROLE_ADMIN, Role.ROLE_USER),
            new User("anna", "anna@yahoo.com", "password", Role.ROLE_USER, null)
    );
}
