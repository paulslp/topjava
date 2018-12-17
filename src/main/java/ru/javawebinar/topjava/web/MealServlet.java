package ru.javawebinar.topjava.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.ConcurrentMealDAO;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TestData;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);


    @Override
    public void init() throws ServletException {
        super.init();
        TestData.fillData();
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConcurrentMealDAO dao = new ConcurrentMealDAO();
        request.setCharacterEncoding("UTF-8");
        String mealId = request.getParameter("mealId");
        String action = request.getParameter("action");

        Meal meal = new Meal(mealId, LocalDateTime.parse(request.getParameter("dateTime")), request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));

        if (action.equals("add")) {
            dao.add(meal);
        } else {
            dao.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ConcurrentMealDAO dao = new ConcurrentMealDAO();

        String action = request.getParameter("action");
        if (action == null) {

            List<Meal> mealList = dao.getAllSorted();
            List<MealWithExceed> mealWithExceededList = MealsUtil.getFilteredWithExceededWithoutTimeFilter(mealList, 2000);
            request.setAttribute("mealWithExceededList", mealWithExceededList);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        String mealId = request.getParameter("mealId");

        switch (action) {
            case "delete":
//2018-12-17 16:37
                dao.delete(mealId);
                response.sendRedirect("meals");
                return;
            case "add":
            case "edit":


                Meal meal = (action.equals("edit")) ? dao.getById(mealId) : new Meal(UUID.randomUUID().toString(),LocalDateTime.of(LocalDate.now(), LocalTime.MIN), "", 0);
                request.setAttribute("meal", meal);
                request.setAttribute("action", action);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
        }
    }
}
