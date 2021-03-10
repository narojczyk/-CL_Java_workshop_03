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

@WebServlet("/user/edit")
public class UserEdit extends HttpServlet {
    private String mrkRed_L = "";
    private String mrkRed_E = "";
    private String mrkRed_P = "";

    protected void doPost(
            HttpServletRequest RX, HttpServletResponse TX)
            throws ServletException, IOException {

        String editUserID = RX.getParameter("modifyID");
        Integer editID = Integer.parseInt(editUserID);

        // Parameters from gatherUserData form
        String FRM_login = RX.getParameter("login");
        String FRM_email = RX.getParameter("email");
        String FRM_name = RX.getParameter("name");
        String FRM_passwd_A = RX.getParameter("fPasswdA");
        String FRM_passwd_B = RX.getParameter("fPasswdB");
        int updStatLogin, updStatEmail, updStatName;
        boolean loginIsValid = false, emailIsValid = false;

        // Resetuj zaznaczenia błędów w formularzu
        mrkRed_L = "";
        mrkRed_E = "";
        mrkRed_P = "";

        UserDao uDAO = new UserDao(SQL_DATABASE_NAME, SQL_TABLE_NAME);

        // Waliduj i zaktualizuj login uzytkownika. Zaznacz pole na czerwono w przypadku bledu
        updStatLogin = 1;
        if(FRM_login!=null && FRM_login.length()>0) {
            loginIsValid = uDAO.validateNewLogin(FRM_login);
            if(loginIsValid && uDAO.update(editID, "login", FRM_login) != DAO_UPDATE_FAILED){
                mrkRed_L = "";
            }else{
                mrkRed_L = "class=\"setRedBrd\"";
                updStatLogin = DAO_UPDATE_FAILED;
            }
        }
        // Waliduj i zaktualizuj email uzytkownika. Zaznacz pole na czerwono w przypadku bledu
        updStatEmail = 1;
        if(FRM_email!=null && FRM_email.length()>0) {
            emailIsValid = uDAO.validateNewEmail(FRM_email);
            if(emailIsValid && uDAO.update(editID, "email", FRM_email) != DAO_UPDATE_FAILED){
                mrkRed_E = "";
            }else{
                mrkRed_E = "class=\"setRedBrd\"";
                updStatEmail = DAO_UPDATE_FAILED;
            }
        }
        // Waliduj (niepusty string) i zaktualizuj name uzytkownika (przytnij string jeśli za długi).
        updStatName = 1;
        if(FRM_name!=null && FRM_name.length()>0) {
            if(FRM_name.length() > SQL_COL_NAME_LENGTH)
                FRM_name = FRM_name.substring(0, SQL_COL_NAME_LENGTH - 1);
            updStatName = uDAO.update(editID, "name", FRM_name);
        }

        // Update password here

        // W przypadku błędów w updatach zaznacz pola na czerwono
        if(updStatLogin + updStatEmail + updStatName != 3){
            System.out.printf("[UserEdit] Update failed L%d E%d N%d\n",
                    updStatLogin , updStatEmail , updStatName);
            TX.sendRedirect(SERVLET_CONTEXT + "/user/edit?uid="+editUserID);
        }else {
            System.out.printf("[UserEdit] Update success L%d E%d N%d\n",
                    updStatLogin , updStatEmail , updStatName);
            TX.sendRedirect(SERVLET_CONTEXT + "/user/list");
        }
    }

    protected void doGet(
            HttpServletRequest RX, HttpServletResponse TX)
            throws ServletException, IOException {

        String getUserID = RX.getParameter("uid");
        Integer editID = Integer.parseInt(getUserID);

        UserDao uDAO = new UserDao(SQL_DATABASE_NAME, SQL_TABLE_NAME);

        if(editID!=null) {
            User userFromSQL = uDAO.read(editID);
            RX.setAttribute("PLH_login", userFromSQL.getLogin());
            RX.setAttribute("PLH_email", userFromSQL.getEmail());
            RX.setAttribute("PLH_name", userFromSQL.getName());
            RX.setAttribute("PLH_passwdA", "password");
            RX.setAttribute("PLH_passwdB", "re-type password");
            RX.setAttribute("editID", editID);
        }

        // Zmienne do zaznaczania kolorem błędów w formularzu
        RX.setAttribute("mrkRed_L", mrkRed_L);
        RX.setAttribute("mrkRed_E", mrkRed_E);
        RX.setAttribute("mrkRed_P", mrkRed_P);

        RX.setAttribute("SRV_CON", SERVLET_CONTEXT);
        RX.setAttribute("ViewName", "Modification form");
        RX.setAttribute("action", SERVLET_CONTEXT+"/user/edit");
        RX.setAttribute("star", " ");
        RX.setAttribute("formInfo", "Modify user data");
        RX.setAttribute("formInstructions","Only fill the fields to be modified");
        getServletContext().getRequestDispatcher("/WEB-INF/gatherUserData.jsp")
                .forward(RX, TX);
    }
}
