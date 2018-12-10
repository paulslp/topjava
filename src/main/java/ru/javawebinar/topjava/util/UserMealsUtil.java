package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;


public class UserMealsUtil {


    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> userMealWithExceedList = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);

        for (UserMealWithExceed userMealWithExceed : userMealWithExceedList) {
            System.out.println(userMealWithExceed.toString());
        }

    }


    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mealDaySum = new HashMap<>();

        int i = 0;

        for (UserMeal userMeal : mealList) {
            LocalDate key = userMeal.getDateTime().toLocalDate();
            mealDaySum.put(key, mealDaySum.getOrDefault(key, 0) + userMeal.getCalories());
        }

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();

        for (UserMeal userMeal : mealList) {
            if (userMeal.getDateTime().toLocalTime().isAfter(startTime)
                    && userMeal.getDateTime().toLocalTime().isBefore(endTime)) {
                userMealWithExceedList.add(new UserMealWithExceed(userMeal.getDateTime()
                        , userMeal.getDescription()
                        , userMeal.getCalories()
                        , mealDaySum.get(userMeal.getDateTime().toLocalDate()) <= 2000));
            }
        }
        return userMealWithExceedList;
    }
}
