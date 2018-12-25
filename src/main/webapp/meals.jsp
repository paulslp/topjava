<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>

<section>
    <h3><a href="index.jsp">Home</a></h3>
    <h2>Meals</h2>

    <form method="post" action="meals">
        <table>
            <tr>
                <td>От даты</td>
                <td>До даты</td>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td>От времени</td>
                <td>До времени</td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td><input type="date" name="dateStart" value="${dStart}"></td>
                <td><input type="date" name="dateEnd" value="${dEnd}"></td>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                <td><input type="time" name="timeStart" value="${tStart}"></td>
                <td><input type="time" name="timeEnd" value="${tEnd}"></td>
                <td>
                    <button type="submit" formmethod="get" name="filter" value="on">Отфильтровать</button>
                </td>
                <td>
                    <button type="submit" formmethod="get" name="filter" value="off">Сбросить фильтр</button>
                </td>

            </tr>
        </table>
    </form>

    <a href="meals?action=create">Add Meal</a>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>