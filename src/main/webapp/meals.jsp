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
            <tr>
                <td>${mealTo.date}</td>
                <td>${mealTo.description}</td>
                <c:choose>
                    <c:when test="${mealTo.excess}">
                        <td><span style="color: red; ">${mealTo.calories}</span></td>
                    </c:when>
                    <c:otherwise>
                        <td><span style="color: green; ">${mealTo.calories}</span></td>
                    </c:otherwise>
                </c:choose>
            </tr>
        </c:forEach>
    </table>
    <br/>
</section>
</body>
</html>
