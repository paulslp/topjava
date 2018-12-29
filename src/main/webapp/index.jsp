<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Java Enterprise (Topjava)</title>
</head>
<body>
<h3>Проект <a href="https://github.com/JavaWebinar/topjava" target="_blank">Java Enterprise (Topjava)</a></h3>
<hr>
<a href="users" >Users</a>
<p>
<form method="post" action="users">
    <button type="submit">Войти как</button>
    <select name="authUserId">
        <option selected value="1">Пользователь 1</option>
        <option value="2">Пользователь 2</option>
    </select>
</form>

</body>
</html>
