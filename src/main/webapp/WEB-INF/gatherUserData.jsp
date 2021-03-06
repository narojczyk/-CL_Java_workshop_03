
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users add</title>
</head>
<body>

<%@ include file="header.jsp" %>

<div>
    <form action="${action}" method="post">
        ${formInfo}
        <label> ${star} <input type="text" name="login" placeholder="${PLH_login}"> </label><br>
        <label> ${star} <input type="text" name="email" placeholder="${PLH_email}"> </label><br>
        <label>   <input type="text" name="name"  placeholder="${PLH_name}"> </label><br>
        <label> ${star} <input type="password" name="fPasswdA" placeholder="${PLH_passwdA}" /></label><br>
        <label> ${star} <input type="password" name="fPasswdB" placeholder="${PLH_passwdB}"/></label><br>

            <input type="hidden" name="modifyID" value="${editID}">

        <input type="submit" value="Wyślij">
    </form>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
