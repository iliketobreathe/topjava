<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Add/Edit record</title>
</head>
<body>
<section>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">

        <dl>
            <dt>Date</dt>
            <dd><input type="text" name="date" size=30 value="${meal.date}"></dd>
        </dl>

        <dl>
            <dt>Time</dt>
            <dd><input type="text" name="time" size=30 value="${meal.time}"></dd>
        </dl>

        <dl>
            <dt>Description</dt>
            <dd><input type="text" name="description" size=30 value="${meal.description}"></dd>
        </dl>

        <dl>
            <dt>Calories</dt>
            <dd><input type="text" name="calories" size=30 value="${meal.calories}"></dd>
        </dl>

        <button type="submit">Сохранить</button>
        <button type="button" onclick="window.history.back()">Отменить</button>
    </form>
</section>
</body>
</html>
