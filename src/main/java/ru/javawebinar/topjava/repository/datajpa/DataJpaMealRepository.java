package ru.javawebinar.topjava.repository.datajpa;

import javassist.runtime.Desc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATE_TIME_DESC = Sort.by(Sort.Direction.DESC, "date_time");

    private final DataJpaUserRepository dataJpaUserRepository;

    private final CrudMealRepository crudRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, DataJpaUserRepository dataJpaUserRepository) {
        this.crudRepository = crudRepository;
        this.dataJpaUserRepository = dataJpaUserRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(dataJpaUserRepository.get(userId));
        if(!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.get(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getAllFiltered(userId, startDateTime, endDateTime);
    }
}
