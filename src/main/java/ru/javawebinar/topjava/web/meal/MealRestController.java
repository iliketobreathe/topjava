package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private MealService service;

    MealRestController(MealService service) {
        this.service = service;
    }

    public List<Meal> getAll() {
        log.info("getAll");
        return service.getAll(authUserId());
    }

    public List<MealTo> getAllByDate(int caloriesByDay, LocalDate startDate, LocalDate endDate) {
        log.info("getAllByDate");
        return service.getAllByDate(getAll(), caloriesByDay, startDate, endDate, authUserId());
    }

    public List<MealTo> getAllByTime(int caloriesByDay, LocalTime startTime, LocalTime  endTime) {
        log.info("getAllByDate");
        return service.getAllByTime(getAll(), caloriesByDay, startTime, endTime, authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        log.info("create {}", meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        log.info("update {} with id={}", meal, id);
        service.update(meal, authUserId());
    }
}