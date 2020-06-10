package ru.javawebinar.topjava.storage.meals;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {
    Meal update(Meal meal);

    Meal save(Meal meal);

    Meal get(int id);

    List<Meal> getAll();

    void delete(int id);
}
