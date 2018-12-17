<%--
  Created by IntelliJ IDEA.
  User: pselyutin
  Date: 17.12.2018
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
<jsp:useBean id="action" type="java.lang.String" scope="request"/>
<html>
<head>
    <title>Еда</title>
</head>
<body>

<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <br>
    <input type="hidden" name="mealId" size=30 value="${meal.id}">
    <dt>Дата/Время</dt>
    <dd><input type="datetime-local" name="dateTime" size=30 value="${(meal==null)?java.time.LocalDateTime.NOW:meal.dateTime}"></dd>
    <br>
    <dt>Описание</dt>
    <dd><input type="text" name="description" size=30 value="${meal.description}"></dd>
    <br>
    <dt>Калории</dt>
    <dd><input type="text" name="calories" size=30 value="${meal.calories}"></dd>
    <br>
    <button type="submit" name="action" value="${action}">
        ${action.equals("edit")?"Изменить":"Добавить"}
    </button>
</form>

</body>
</html>
