package ru.javawebinar.topjava.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ru.javawebinar.topjava.dao.ConcurrentMealDAO;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TestData;

public class MealServlet extends HttpServlet {

    private final MealDao DAO = new ConcurrentMealDAO(new AtomicInteger(1));

    @Override
    public void init() throws ServletException {
        super.init();
        List<Meal> mealList = TestData.fillData();
        mealList.forEach(DAO::add);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action.equals("add")) {
            Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")), request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
            DAO.add(meal);
        } else {
            int mealId = Integer.valueOf(request.getParameter("mealId"));
            Meal meal = new Meal(mealId, LocalDateTime.parse(request.getParameter("dateTime")), request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
            DAO.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<Meal> mealList = DAO.getAll();
            List<MealWithExceed> mealWithExceededList = MealsUtil.getFilteredWithExceeded(mealList, LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("mealWithExceededList", mealWithExceededList);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        switch (action) {
            case "delete":
                DAO.delete(Integer.valueOf(request.getParameter("mealId")));
                response.sendRedirect("meals");
                return;
            case "add":
            case "edit":
                Meal mealForSend = (action.equals("edit")) ? DAO.read(Integer.valueOf(request.getParameter("mealId"))) : new Meal(LocalDateTime.of(LocalDate.now(), LocalTime.MIN), "", 0);
                request.setAttribute("meal", mealForSend);
                request.setAttribute("action", action);
                request.getRequestDispatcher("/meal.jsp").forward(request, response);
                break;
        }
    }
}
