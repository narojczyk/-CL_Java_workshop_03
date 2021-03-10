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
    private String mrkRed_L = "";
    private String mrkRed_E = "";
    private String mrkRed_P = "";

    private String LST_valid_login = "";
    private String LST_valid_email = "";
    private String LST_valid_name = "";
    private String LST_valid_passwd = "";

    protected void doPost(
            HttpServletRequest RX, HttpServletResponse TX)
            throws ServletException, IOException {

        // Parameters from gatherUserData form
        String FRM_login = RX.getParameter("login");
        String FRM_email = RX.getParameter("email");
        String FRM_name = RX.getParameter("name");
        if(FRM_name == null) FRM_name = "";
        if(FRM_name.length() > SQL_COL_NAME_LENGTH)
            FRM_name = FRM_name.substring(0, SQL_COL_NAME_LENGTH - 1);
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
            // Wszystkie pola dobre. Nie zaznaczać nic na czerwono
            mrkRed_L = "";
            mrkRed_E = "";
            mrkRed_P = "";
            LST_valid_login = "";
            LST_valid_email = "";
            LST_valid_name = "";
            LST_valid_passwd = "";

        }else{
            // Pola które nie przeszły testu zostaną zaznaczone na czerwono
            if(!loginIsValid) {
                mrkRed_L = "class=\"setRedBrd\"";
            }else{
                LST_valid_login = FRM_login;
            }
            if(!emailIsValid) {
                mrkRed_E = "class=\"setRedBrd\"";
            }else{
                LST_valid_email = FRM_email;
            }
            LST_valid_name = FRM_name;
            if(!passwdMathes || !strongPasswd) {
                mrkRed_P = "class=\"setRedBrd\"";
            }else{
                LST_valid_passwd = FRM_passwd_B;
            }
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

        // Zmienne do zaznaczania kolorem błędów w formularzu
        RX.setAttribute("mrkRed_L", mrkRed_L);
        RX.setAttribute("mrkRed_E", mrkRed_E);
        RX.setAttribute("mrkRed_P", mrkRed_P);
        // Zmienne zapamiętujące wprowadzonen poprawnie dane
        RX.setAttribute("LST_val_L", LST_valid_login);
        RX.setAttribute("LST_val_E", LST_valid_email);
        RX.setAttribute("LST_val_N", LST_valid_name);
        RX.setAttribute("LST_val_P", LST_valid_passwd);

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
