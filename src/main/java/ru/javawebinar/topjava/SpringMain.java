package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {

        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
//            mealRestController.getAllOurMeal();
//            mealRestController.getAll();
//            mealRestController.get(2);
//            mealRestController.get(10);
//            mealRestController.get(15);
            mealRestController.update(new Meal(SecurityUtil.authUserId(), LocalDateTime.now(), "", 2000), 3);
            mealRestController.getAllOurMeal();
            mealRestController.create(new Meal(SecurityUtil.authUserId(), LocalDateTime.now(), "", 2000));
            mealRestController.getAllOurMeal();
            mealRestController.update(new Meal(SecurityUtil.authUserId(), LocalDateTime.now(), "", 2000), 7);


        }
    }
}
