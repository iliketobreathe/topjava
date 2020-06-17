package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    //    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

/*    {
        MealsUtil.MEALS.forEach(meal -> {
            meal.setId(counter.incrementAndGet());
            repository.put(counter.get(), meal);
        });
    }*/

    {
        MealsUtil.MEALS.forEach(meal -> {
            meal.setId(counter.incrementAndGet());
            if (!repository.containsKey(meal.getUserId())) {
                repository.put(meal.getUserId(), new HashMap<>());
            }
            repository.get(meal.getUserId()).put(meal.getId(), meal);
        });
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            if (!repository.containsKey(userId)) {
                repository.put(userId, new HashMap<>());
            }
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        } else if (repository.get(meal.getUserId()) != null && meal.getUserId() == userId) {
            return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        throw new NotFoundException("Another userId");
    }

    @Override
    public boolean delete(int id, int userId) {
        try {
            return repository.get(userId).remove(id) != null;
        } catch (NullPointerException ex) {
            throw new NotFoundException("Meal with id is not found");
        }
    }

    @Override
    public Meal get(int id, int userId) {
        try {
            return repository.get(userId).get(id);
        } catch (NullPointerException ex) {
            throw new NotFoundException("Meal with id is not found");
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.get(userId) != null ? repository.get(userId).values().stream()
//                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()) :
                new ArrayList<Meal>();
    }

    @Override
    public List<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.get(userId).values().stream()
//                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .filter(meal -> DateTimeUtil.isBetweenInclusive(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

