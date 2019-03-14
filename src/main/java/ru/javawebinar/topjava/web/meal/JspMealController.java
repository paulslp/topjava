package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.Util.orElse;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController {
    @Autowired
    private MealService service;

    @GetMapping("")
    public String getAll(Model model) {
        int userId = SecurityUtil.authUserId();
        model.addAttribute("meals", MealsUtil.getWithExcess(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay()));
        return "/meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        service.delete(Integer.valueOf(request.getParameter("id")), SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @GetMapping({"/create", "/update"})
    public String showMealFormForCreate(Model model, HttpServletRequest request) {
        Meal meal = (request.getRequestURI().endsWith("create")) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                service.get(Integer.valueOf(request.getParameter("id")), SecurityUtil.authUserId());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("meals")
    public String update(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if ("".equals(request.getParameter("id"))) {
            service.create(meal, SecurityUtil.authUserId());
        } else {
            meal.setId(Integer.valueOf(request.getParameter("id")));
            service.update(meal, SecurityUtil.authUserId());
        }

        return "redirect:/meals";
    }

    @PostMapping("/filter")
    public String getBetween(Model model, HttpServletRequest request) {
        LocalDate startDate = orElse(parseLocalDate(request.getParameter("startDate")),LocalDate.of(1970,1,1));
        LocalDate endDate = orElse(parseLocalDate(request.getParameter("endDate")),LocalDate.now());
        LocalTime startTime = orElse(parseLocalTime(request.getParameter("startTime")),LocalTime.MIN);
        LocalTime endTime = orElse(parseLocalTime(request.getParameter("endTime")),LocalTime.MAX);
        List<Meal> mealsDateFiltered = service.getBetweenDateTimes(startDate.atTime(startTime), endDate.atTime(endTime), SecurityUtil.authUserId());

        model.addAttribute("meals", MealsUtil.getFilteredWithExcess(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(),
                startTime, endTime));
        return "/meals";
    }

}
