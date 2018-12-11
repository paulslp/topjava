package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


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
            System.out.println(userMealWithExceed);
        }

        System.out.println("--------------withStream--------------");

        userMealWithExceedList = getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(14, 0), 2000);

        for (UserMealWithExceed userMealWithExceed : userMealWithExceedList) {
            System.out.println(userMealWithExceed);
        }
    }


    private static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mealDaySum = new HashMap<>();


        for (UserMeal userMeal : mealList) {
            LocalDate key = userMeal.getDateTime().toLocalDate();
            mealDaySum.put(key, mealDaySum.getOrDefault(key, 0) + userMeal.getCalories());
        }

        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();

        for (UserMeal userMeal : mealList) {
            if (userMeal.getDateTime().toLocalTime().compareTo(startTime) > -1
                    && userMeal.getDateTime().toLocalTime().compareTo(endTime) < 1) {
                userMealWithExceedList.add(new UserMealWithExceed(userMeal.getDateTime()
                        , userMeal.getDescription()
                        , userMeal.getCalories()
                        , mealDaySum.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return userMealWithExceedList;
    }

    private static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {


        Map<LocalDate, Integer> mealDaySum = mealList.stream()
                .collect(Collectors.toMap(userMeal -> userMeal.getDateTime().toLocalDate()
                        , UserMeal::getCalories
                        , Integer::sum));

        return mealList.stream()
                .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExceed(userMeal.getDateTime()
                        , userMeal.getDescription()
                        , userMeal.getCalories()
                        , mealDaySum.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());


    }
}
