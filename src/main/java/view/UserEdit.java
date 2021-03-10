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

        UserDao uDAO = new UserDao(SQL_DATABASE_NAME, SQL_TABLE_NAME);

        if(FRM_login!=null && uDAO.validateNewLogin(FRM_login)) {
            uDAO.update(editID, "login", FRM_login);
        }

        if(FRM_email!=null && uDAO.validateNewEmail(FRM_email)) {
            uDAO.update(editID, "email", FRM_email);
        }

        if(FRM_name!=null) {
            uDAO.update(editID, "name", FRM_name);
        }

        // Update password here

        TX.sendRedirect(SERVLET_CONTEXT+"/user/list");
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

        /*RX.setAttribute("LST_login", "");
        RX.setAttribute("LST_email", "");
        RX.setAttribute("LST_name", "");*/
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
