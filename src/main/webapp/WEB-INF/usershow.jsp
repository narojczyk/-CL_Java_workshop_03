
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<style>
    span {
        white-space: nowrap;
    }
</style>
<head>
    <title>Users data</title>
</head>
<body>

<%@ include file="header.jsp" %>

<div>
    <table>
      <tbody>
      <tr><th>ID      </th><td><option value="${UserData}">${UserData.getID()}</td></tr>
      <tr><th>Login   </th><td><option value="${UserData}">${UserData.getLogin()}</td></tr>
      <tr><th>Nazwisko</th><td><option value="${UserData}">${UserData.getName()}</td></tr>
      <tr><th>Email   </th><td><option value="${UserData}">${UserData.getEmail()}</td></tr>
      </tbody>
    </table>
</div>
    <form action="/user/edit" method="post">
        <input type="hidden" name="modifyID" value="${UserData.getID()}">
        <input type="submit" value="Modyfikuj użytkownika">
    </form>

<%@ include file="footer.jsp" %>

</body>
</html>
