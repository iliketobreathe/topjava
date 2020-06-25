package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"), executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealRepository repository;

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(YOUR_MEAL_ID, YOUR_USER_ID);
        assertMatch(meal, YOUR_MEAL);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, YOUR_USER_ID));
    }

    @Test
    public void delete() {
        service.delete(YOUR_MEAL_ID, YOUR_USER_ID);
        assertNull(repository.get(YOUR_MEAL_ID, YOUR_USER_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(FOREIGN_MEAL_ID, YOUR_USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(START_DATE, END_DATE, YOUR_USER_ID), MEALS_FILTERED);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(YOUR_USER_ID);
        assertMatch(all, MEALS);
    }

    @Test
    public void update() {
        Meal updated = getUpdated(YOUR_MEAL);
        service.update(updated, YOUR_USER_ID);
        assertMatch(service.get(YOUR_MEAL_ID, YOUR_USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateForeign() {
        Meal updated = getUpdated(FOREIGN_MEAL);
        service.update(updated, YOUR_USER_ID);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, YOUR_USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, YOUR_USER_ID), newMeal);
    }


}