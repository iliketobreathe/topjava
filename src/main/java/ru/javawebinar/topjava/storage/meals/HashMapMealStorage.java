package ru.javawebinar.topjava.storage.meals;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapMealStorage implements MealStorage {
    private Map<Integer, Meal> map = new ConcurrentHashMap<>();

    public HashMapMealStorage(List<Meal> mealsList) {
        mealsList.forEach(meal -> map.put(meal.getId(), meal));
    }

    public HashMapMealStorage() {
    }

    @Override
    public Meal update(Meal meal) {
        int id = meal.getId();
        if (map.containsKey(id)) {
            map.put(id, meal);
            return meal;
        }
        return null;
    }

    @Override
    public Meal save(Meal meal) {
        int id = meal.getId();
        if (!map.containsKey(id)) {
            map.put(id, meal);
            return meal;
        }
        return null;
    }

    @Override
    public Meal get(int id) {
        return map.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public void delete(int id) {
        map.remove(id);
    }
}
