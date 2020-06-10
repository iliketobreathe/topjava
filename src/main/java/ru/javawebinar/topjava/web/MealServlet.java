package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.meals.HashMapMealStorage;
import ru.javawebinar.topjava.storage.meals.MealStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static MealStorage storage;

    @Override
    public void init() throws ServletException {
        storage = new HashMapMealStorage(MealsUtil.meals);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");

        if (storage.get(Integer.parseInt(id)) != null) {
            storage.update(new Meal(LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time)), description, Integer.parseInt(calories)));
        } else {
            storage.save(new Meal(LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time)), description, Integer.parseInt(calories)));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get all meals");
        String id = (request.getParameter("id"));
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("mealsTo", MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.MAX_CALORIES));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        Meal meal;
        switch (action) {
            case "delete":
                storage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                return;
            case "edit":
                meal = storage.get(Integer.parseInt(id));
                break;
            case "add":
                meal = new Meal(LocalDateTime.now(), "", 0);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }
}