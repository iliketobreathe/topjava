<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Meal ID: ${meal.id}</title>
</head>
<body>
<section>
    Date: ${meal.date}
    Time: ${meal.time}
    Description: ${meal.description}
    Calories: ${meal.calories}
</section>
</body>
</html>