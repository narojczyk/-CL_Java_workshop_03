
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@ include file="common_head.html" %>
    <title>Users add</title>
</head>
<body>

<div class="mainbody">
    <%@ include file="header.jsp" %>

    <div class="menufs">
        <fieldset class="menufs">
            <legend class="menufsdesc">&nbsp;${formInfo}:&nbsp;</legend>
                <form action="${action}" method="post">

            <label> <input ${mrkRed_L} type="text" name="login" placeholder="${PLH_login}"
                                       value="${LST_val_L}">${star}
                <span class="menufsdesc">Unique, 5 or more alfanumeric characters, minus or underscore are
                    allowed (cannot begin with a number).</span></label> <br>

            <label> <input ${mrkRed_E} type="text" name="email" placeholder="${PLH_email}"
                                       value="${LST_val_E}">${star}
                <span class="menufsdesc">Unique, valid email form.</span></label><br>

            <label> <input  type="text" name="name"  placeholder="${PLH_name}" value="${LST_val_N}"> </label><br>

            <label> <input ${mrkRed_P} type="password" name="fPasswdA" placeholder="${PLH_passwdA}"
                                       value="${LST_val_P}">${star}
                <span class="menufsdesc">Min. 8 characters. Must contain at least one lowercase and
                    uppercase letter, at least one number and special character.</span></label><br>

            <label> <input ${mrkRed_P} type="password" name="fPasswdB" placeholder="${PLH_passwdB}"
                                       value="${LST_val_P}">${star}
                <span class="menufsdesc">Passwords must match.</span></label><br>

            <input type="hidden" name="modifyID" value="${editID}">
            <p class="menufsdesc">${formInstructions}</p>

            <input class="pushbutton" type="submit" value="Submit form">
        </form></fieldset></div>

    <%@ include file="footer.jsp" %>
</div>
</body>
</html>
