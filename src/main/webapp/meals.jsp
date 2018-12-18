<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<form method="get" action="meals" enctype="application/x-www-form-urlencoded">
    <button type="submit" name="action" value="add">
        Добавить
    </button>
</form>

<table>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>

    <c:forEach var="mealWithExceeded" items="${requestScope.mealWithExceededList}">
        <tr bgcolor=<c:out value="${mealWithExceeded.isExceed() ?'#f08080':'#90ee90'}"/>>
            <td>${mealWithExceeded.getDateTime().toString().replace("T"," ")}</td>
            <td>${mealWithExceeded.getDescription()}</td>
            <td> ${mealWithExceeded.getCalories()}</td>
            <td><a href="meals?mealId=${mealWithExceeded.getId()}&action=edit"><img src="img/pencil.png"></a></td>
            <td><a href="meals?mealId=${mealWithExceeded.getId()}&action=delete"><img src="img/delete.png"></a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>