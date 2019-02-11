package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID1 = START_SEQ + 2;
    public static final int MEAL_ID2 = START_SEQ + 3;
    public static final int MEAL_ID3 = START_SEQ + 4;
    public static final int MEAL_ID4 = START_SEQ + 5;

    public static final int MEAL_ID5 = START_SEQ + 6;
    public static final int MEAL_ID6 = START_SEQ + 7;
    public static final int MEAL_ID7 = START_SEQ + 8;
    public static final int MEAL_ID8 = START_SEQ + 9;


    public static final Meal MEAL1 = new Meal(MEAL_ID1, LocalDateTime.of(2019, 2, 4, 8, 0), null, 400);
    public static final Meal MEAL2 = new Meal(MEAL_ID2, LocalDateTime.of(2019, 2, 4, 20, 0), null, 400);
    public static final Meal MEAL3 = new Meal(MEAL_ID3, LocalDateTime.of(2019, 2, 5, 14, 0), "description4", 300);
    public static final Meal MEAL4 = new Meal(MEAL_ID4, LocalDateTime.of(2019, 2, 5, 16, 0), "description5", 2000);

    public static final Meal MEAL5 = new Meal(MEAL_ID5, LocalDateTime.of(2019, 1, 4, 8, 0), null, 2000);
    public static final Meal MEAL6 = new Meal(MEAL_ID6, LocalDateTime.of(2019, 1, 4, 20, 0), null, 400);
    public static final Meal MEAL7 = new Meal(MEAL_ID7, LocalDateTime.of(2019, 1, 5, 10, 0), "description2", 100);
    public static final Meal MEAL8 = new Meal(MEAL_ID8, LocalDateTime.of(2019, 1, 5, 14, 0), "description4", 200);


    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingComparatorForFields(Comparator.comparing(LocalDateTime::toString), "datetime").isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual)
                .usingFieldByFieldElementComparator()
                .isEqualTo(expected);
    }

}
