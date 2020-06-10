package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    Storage storage = MealsUtil.storage;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get all meals");
        String id = (request.getParameter("id"));
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("mealsTo", MealsUtil.filteredByStreams(MealsUtil.storage, LocalTime.MIN, LocalTime.MAX, MealsUtil.MAX_CALORIES));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        Meal meal;
        switch (action) {
            case "delete":
                storage.delete(Integer.parseInt(id));
                response.sendRedirect("meals");
                return;
            case "view":
            case "edit":
                meal = storage.get(Integer.parseInt(id));
                break;
            case "add":
                meal = new Meal(LocalDateTime.MIN, "", 0);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("view".equals(action) ? "/view.jsp" : "/edit.jsp").forward(request, response);
    }
}