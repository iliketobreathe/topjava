<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<section>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Дата</th>
            <th>Описание</th>
            <th>Калории</th>
        </tr>
        <c:forEach items="${mealsTo}" var="mealTo">
            <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
            <c:set var="color"
                   value="${mealTo.excess ? 'red' : 'green'}"/>
            <tr style="color: ${color}">
                <td>${mealTo.date} ${mealTo.time}</td>
                <td>${mealTo.description}</td>
                <td>${mealTo.calories}</td>
            </tr>
        </c:forEach>
    </table>
    <br/>
</section>
</body>
</html>
