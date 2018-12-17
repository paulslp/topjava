package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static void main(String[] args) {
//        LocalDateTime dateTime = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd ");
//        String text = dateTime.format(formatter);
//        LocalDateTime parsedDateTime = LocalDateTime.parse(text, formatter);



        System.out.println(LocalDateTime.of(LocalDate.now(),LocalTime.MIN));
    }
}
