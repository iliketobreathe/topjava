package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController {

    @Autowired
    private MealRestController controller;

    @GetMapping("/meals")
    public String getMeals(Model model) {
        model.addAttribute("meals", controller.getAll());
        return "meals";
    }

    @GetMapping("/mealsFiltered")
    public String getMealsFiltered(HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        request.setAttribute("meals", controller.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        controller.delete(id);
        return "redirect:meals";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request) {
        request.setAttribute("meal", controller.get(Integer.parseInt(request.getParameter("id"))));
        return "mealForm";
    }

    @GetMapping("/create")
    public String create(HttpServletRequest request) {
        request.setAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @PostMapping("/update")
    public String updateMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (StringUtils.isEmpty(request.getParameter("id"))) {
            controller.create(meal);
        } else {
            controller.update(meal, Integer.parseInt(request.getParameter("id")));
        }
        return "redirect:meals";
    }
}
