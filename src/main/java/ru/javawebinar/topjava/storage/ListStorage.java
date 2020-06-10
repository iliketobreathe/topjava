package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class ListStorage implements Storage {
    private List<Meal> list = new ArrayList<>();

    public ListStorage(List<Meal> mealsList) {
        list.addAll(mealsList);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void update(Meal meal) {
        int id = meal.getId();
        for (Meal mealForUpd : list) {
            if (id == mealForUpd.getId()) {
                mealForUpd.setCalories(meal.getCalories());
                mealForUpd.setDateTime(meal.getDateTime());
                mealForUpd.setDescription(meal.getDescription());
                return;
            }
        }
    }

    @Override
    public void save(Meal meal) {
        for (Meal meal1 : list) {
            if (meal.equals(meal1)) {
                return;
            }
        }
        list.add(meal);
    }

    @Override
    public Meal get(int id) {
        for (Meal meal : list) {
            if (meal.getId() == id) return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAll() {
        return list;
    }

    @Override
    public void delete(int id) {
        for (int i = 0; i < list.size(); i++) {
            int mealId = list.get(i).getId();
            if (mealId == id) {
                list.remove(i);
                return;
            }
        }
    }

    @Override
    public int size() {
        return list.size();
    }
}
