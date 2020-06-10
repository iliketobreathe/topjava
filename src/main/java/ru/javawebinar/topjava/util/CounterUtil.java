package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

public class CounterUtil {
    private static int count = 1;

    public static int countIncrement() {
        synchronized (Meal.class) {
            return count++;
        }
    }
}
