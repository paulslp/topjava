package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;

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

    @GetMapping({"/create"})
    public String showMealFormForCreate(Model model, HttpServletRequest request) {
        return addMealAttrributeAndShowForm(model, new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
    }

    @GetMapping({"/update"})
    public String showMealFormForUpdate(Model model, HttpServletRequest request) {
        return addMealAttrributeAndShowForm(model, super.get(Integer.valueOf(request.getParameter("id"))));
    }

    @PostMapping("insert")
    public String create(HttpServletRequest request) {
        super.create(createMealFromRequestData(request));
        return "redirect:/meals";
    }

    private Meal createMealFromRequestData(HttpServletRequest request) {
        return new Meal(request.getParameter("id").isEmpty() ? null : Integer.valueOf(request.getParameter("id")),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
    }

    private String addMealAttrributeAndShowForm(Model model, Meal meal) {
        model.addAttribute(meal);
        return "mealForm";
    }


    @PostMapping("update")
    public String update(HttpServletRequest request) {
        super.update(createMealFromRequestData(request), Integer.valueOf(request.getParameter("id")));
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

        return "meals";
    }

}
