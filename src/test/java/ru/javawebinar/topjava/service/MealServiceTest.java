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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

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
    MealRepository repository;

    @Autowired
    MealService service;

    @Test
    public void get() {
        Meal meal = service.get(YOUR_MEAL_ID, authUserId());
        assertThat(meal).isEqualToComparingFieldByField(YOUR_MEAL);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, authUserId()));
    }

    @Test
    public void delete() {
        service.delete(YOUR_MEAL_ID, authUserId());
        assertNull(repository.get(YOUR_MEAL_ID, authUserId()));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(FOREIGN_MEAL_ID, authUserId()));
    }

    @Test
    public void getBetweenInclusive() {
        assertThat(service.getBetweenInclusive(START_DATE, END_DATE, authUserId())).usingElementComparatorIgnoringFields("id").isEqualTo(MEALS_FILTERED);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(authUserId());
        assertThat(all).usingElementComparatorIgnoringFields("id").isEqualTo(MEALS);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, authUserId());
        assertThat(service.get(YOUR_MEAL_ID, authUserId())).isEqualToComparingFieldByField(updated);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, authUserId());
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertThat(created).isEqualToComparingFieldByField(newMeal);
        assertThat(service.get(newId, authUserId())).isEqualToComparingFieldByField(newMeal);
    }


}