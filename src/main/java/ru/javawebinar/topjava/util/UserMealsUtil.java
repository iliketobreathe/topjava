package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo1 = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo1.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo2 = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo2.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo3 = filteredByOneCycle(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo3.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDay = new HashMap<>();
        meals.forEach(userMeal -> {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            caloriesByDay.merge(localDate, userMeal.getCalories(), Integer::sum);
        });

        List<UserMealWithExcess> mealsWithExcesses = new ArrayList<>();
        meals.forEach(userMeal -> addMealWithExcess(mealsWithExcesses, caloriesByDay, userMeal, startTime, endTime, caloriesPerDay));
        return mealsWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> caloriesByDay = meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> {
                    boolean isExceed = caloriesByDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay;
                    return new UserMealWithExcess(userMeal, isExceed);
                })
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByOneCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsWithExcesses = new ArrayList<>();
        Map<LocalDate, Integer> caloriesByDay = new HashMap<>();
        recursiveMethod(meals, mealsWithExcesses, caloriesByDay, startTime, endTime, caloriesPerDay, meals.size(), 0);
        return mealsWithExcesses;
    }

    private static void recursiveMethod(List<UserMeal> meals, List<UserMealWithExcess> mealsWithExcesses, Map<LocalDate, Integer> caloriesByDay, LocalTime startTime, LocalTime endTime, int caloriesPerDay, int mealsSize, int mealIndex) {
        if (mealIndex < mealsSize) {
            UserMeal userMeal = meals.get(mealIndex);
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            caloriesByDay.merge(localDate, userMeal.getCalories(), Integer::sum);
            recursiveMethod(meals, mealsWithExcesses, caloriesByDay, startTime, endTime, caloriesPerDay, mealsSize, ++mealIndex);
            addMealWithExcess(mealsWithExcesses, caloriesByDay, userMeal, startTime, endTime, caloriesPerDay);
        }
    }

    private static void addMealWithExcess(List<UserMealWithExcess> mealsWithExcesses, Map<LocalDate, Integer> caloriesByDay, UserMeal userMeal, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        LocalDate localDate = userMeal.getDateTime().toLocalDate();
        LocalTime localTime = userMeal.getDateTime().toLocalTime();
        boolean isExceed = caloriesByDay.get(localDate) > caloriesPerDay;
        if (TimeUtil.isBetweenHalfOpen(localTime, startTime, endTime)) {
            mealsWithExcesses.add(new UserMealWithExcess(userMeal, isExceed));
        }
    }
}
