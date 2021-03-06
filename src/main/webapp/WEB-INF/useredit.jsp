
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users edit</title>
</head>
<body>

<%@ include file="header.jsp" %>

<div>
    <form action="/user/add" method="post">
        <p>new user form</p>
        <p>required fields are marked with(*)</p>
        <label> * <input type="text" name="login" value="${LST_login}" placeholder="login"> </label><br>
        <label> * <input type="text" name="email" value="${LST_email}" placeholder="email"> </label><br>
        <label>   <input type="text" name="name" value="${LST_name}"  placeholder="full name"> </label><br>
        <label> * <input type="password" name="fPasswdA" placeholder="password" /></label><br>
        <label> * <input type="password" name="fPasswdB" placeholder="retype password"/></label><br>

        <input type="submit" value="Wyślij">
    </form>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
