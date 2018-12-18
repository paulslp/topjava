package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;


public class TestData {

    public static List<Meal> fillData() {
        return Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 29, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 19, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 29, 11, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 29, 20, 0), "Ужин", 510)
        );
    }
}
