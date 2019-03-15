package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {


    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "/meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        super.delete(Integer.valueOf(request.getParameter("id")));
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
        model.addAttribute("meals",
                super.getBetween(parseLocalDate(request.getParameter("startDate")),
                        parseLocalTime(request.getParameter("startTime")),
                        parseLocalDate(request.getParameter("endDate")),
                        parseLocalTime(request.getParameter("endTime")))
        );
        return "/meals";
    }

}
