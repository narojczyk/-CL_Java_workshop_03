
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<style>
    span {
        white-space: nowrap;
    }
</style>
<head>
    <%@ include file="common_head.html" %>
    <title>Users data</title>
</head>
<body>
<div class="mainbody">
    <%@ include file="header.jsp" %>

<div class="menufs">
    <table>
      <tbody>
      <tr><th style="text-align:right;">ID :</th>
          <td><option value="${UserData}">${UserData.getID()}</td></tr>
      <tr><th style="text-align:right;">Login :</th>
          <td><option value="${UserData}">${UserData.getLogin()}</td></tr>
      <tr><th style="text-align:right;">Nazwisko :</th>
          <td><option value="${UserData}">${UserData.getName()}</td></tr>
      <tr><th style="text-align:right;">Email :</th>
          <td><option value="${UserData}">${UserData.getEmail()}</td></tr>
      </tbody>
    </table>

    <form action="${action}" method="${method}">
        <input type="hidden" name="uid" value="${UserData.getID()}">
        <input class="pushbutton${mkRed}"  type="submit" value="${actionDesc}">
    </form>
</div>

<%@ include file="footer.jsp" %>
</div>
</body>
</html>
