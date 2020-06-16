package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal, int userId);

    // false if not found
    boolean delete(int id, int userId);

    // null if not found
    Meal get(int id, int userId);

    List<Meal> getAll(int userId);

    List<MealTo> getAllByDate(List<Meal> mealsList, int caloriesPerDay, LocalDate startDate, LocalDate endDate,  int userId);

    List<MealTo> getAllByTime(List<Meal> mealsList, int caloriesPerDay, LocalTime startTime, LocalTime endTime, int userId);

    //<T extends LocalDateTime> List<Meal> getAllByDate(List<Meal> mealsList, T start, T end, int userId);
}
