
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Users list</title>
</head>
<body>

<%@ include file="header.jsp" %>

<div>
    <table>
      <thead>
        <tr><th>ID</th><th>Login</th><th>Email</th><th>Akcja</th>
      </thead><tbody>
      <c:forEach items="${UsersMap}" var="UsersMap">
        <tr>
            <option value="${UsersMap.key}">
                <td>${UsersMap.value.getID()}   </td>
                <td>${UsersMap.value.getLogin()}</td>
                <td>${UsersMap.value.getEmail()}</td>
            </option>
            <td>    <a href="/user/delete?uid=${UsersMap.value.getID()}">Usuń</a>
                    <a href="/user/edit?uid=${UsersMap.value.getID()}">Edytuj</a>
                    <a href="/user/show?uid=${UsersMap.value.getID()}">Pokaż</a></td>
        </tr>
        </c:forEach>
      </tbody>
    </table>

    <form action="/user/add" method="get">
        <input type="submit" value="Dodaj użytkownika">
    </form>
</div>

<%@ include file="footer.jsp" %>

</body>
</html>
