
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <%@ include file="common_head.html" %>
    <title>Users list</title>
</head>
<body>
<div class="mainbody">
    <%@ include file="header.jsp" %>
    <div>
        <table>
          <thead>
            <tr><th style="width: 10%;">ID</th>
                <th style="width: 25%;">Login</th>
                <th style="width: auto;">Email</th>
                <th style="width: 25%; min-width:260px;">Action</th>
          </thead><tbody>
          <c:forEach items="${UsersMap}" var="UsersMap">
            <tr>
                <option value="${UsersMap.key}">
                    <td>${UsersMap.value.getID()}   </td>
                    <td>${UsersMap.value.getLogin()}</td>
                    <td>${UsersMap.value.getEmail()}</td>
                </option>
                <td>    <a href="${SRV_CON}/user/delete?uid=${UsersMap.value.getID()}">Delete</a>
                        <a href="${SRV_CON}/user/edit?uid=${UsersMap.value.getID()}">Edit</a>
                        <a href="${SRV_CON}/user/show?uid=${UsersMap.value.getID()}">Show details</a></td>
            </tr>
          </c:forEach>
          </tbody>
        </table>

        <form action="${SRV_CON}/user/add" method="get">
            <input class="pushbutton" type="submit" value="Add user">
        </form>
    </div>

    <%@ include file="footer.jsp" %>
</div>
</body>
</html>
