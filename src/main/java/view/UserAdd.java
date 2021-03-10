package view;

import ctrl.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ctrl.Parameters.*;

@WebServlet("/user/add")
public class UserAdd extends HttpServlet {
    protected void doPost(
            HttpServletRequest RX, HttpServletResponse TX)
            throws ServletException, IOException {

        // Parameters from gatherUserData form
        String FRM_login = RX.getParameter("login");
        String FRM_email = RX.getParameter("email");
        String FRM_name = RX.getParameter("name");
        if(FRM_name == null) FRM_name="";
        String FRM_passwd_A = RX.getParameter("fPasswdA");
        String FRM_passwd_B = RX.getParameter("fPasswdB");

        UserDao uDAO = new UserDao(SQL_DATABASE_NAME, SQL_TABLE_NAME);

        boolean loginIsValid = uDAO.validateNewLogin(FRM_login);
        boolean emailIsValid = uDAO.validateNewEmail(FRM_email);
        boolean passwdMathes = FRM_passwd_A.equals(FRM_passwd_B);
        boolean strongPasswd = uDAO.testPasswdStrength(FRM_passwd_A);

        long ID = 0;
        if(true && loginIsValid && emailIsValid && passwdMathes && strongPasswd) {
            ID = uDAO.create(new User(FRM_login, FRM_name, FRM_email, FRM_passwd_A));
            // To nie działa
            RX.setAttribute("mrkRed_L", "");
            RX.setAttribute("mrkRed_E", "");
            RX.setAttribute("mrkRed_P", "");
        }else{
            // To nie działa
            if(!loginIsValid) RX.setAttribute("mrkRed_L", "class=\"setRedBrd\"");
            if(!emailIsValid) RX.setAttribute("mrkRed_E", "class=\"setRedBrd\"");
            if(!passwdMathes || !strongPasswd)
                RX.setAttribute("mrkRed_P", "class=\"setRedBrd\"");
        }

        if(ID == DAO_CREATE_FAILED){
            System.out.println("[UserAdd] User NOT added - 'null' passed to create() ID="+ID);
        }else if (ID <= 0){
            System.out.printf("[UserAdd] User NOT added ID=%d tests: ",ID);
            System.out.printf("login %s;", loginIsValid);
            System.out.printf("email %s;", emailIsValid);
            System.out.printf("passwdMatch %s;", passwdMathes);
            System.out.printf("passwdStrong %s;\n", strongPasswd);
            TX.sendRedirect(SERVLET_CONTEXT+"/user/add");
        }else{
            System.out.printf("[UserAdd] User added to %s with ID=%d\n", SQL_DATABASE_NAME, ID);
            TX.sendRedirect(SERVLET_CONTEXT+"/user/list");
        }
    }

    protected void doGet(
            HttpServletRequest RX, HttpServletResponse TX)
            throws ServletException, IOException {
        // Wyswietl formularz do zebrania danych o uzytkownika
        RX.setAttribute("SRV_CON", SERVLET_CONTEXT);
        RX.setAttribute("ViewName", "Registration form");
        RX.setAttribute("action", SERVLET_CONTEXT+"/user/add");
        RX.setAttribute("formInfo", "New user data");
        RX.setAttribute("formInstructions", "Fields marked with (*) are required");

        // To sie przyda jak zastosuje cookies
//        RX.setAttribute("mrkRed_L", "class=\"setRedBrd\"");
//        RX.setAttribute("mrkRed_E", "");
//        RX.setAttribute("mrkRed_P", "");

        RX.setAttribute("PLH_login", "login");
        RX.setAttribute("PLH_email", "email");
        RX.setAttribute("PLH_name", "full name");
        RX.setAttribute("PLH_passwdA", "password");
        RX.setAttribute("PLH_passwdB", "re-type password");
        RX.setAttribute("editID", "0");

        RX.setAttribute("star", "*");
        getServletContext().getRequestDispatcher("/WEB-INF/gatherUserData.jsp")
                .forward(RX, TX);
    }
}
