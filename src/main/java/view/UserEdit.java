package view;

import ctrl.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/edit")
public class UserEdit extends HttpServlet {
    protected void doPost(
            HttpServletRequest r, HttpServletResponse R)
            throws ServletException, IOException {

        final String dbName = "workshop3", dbTable = "users";

        String editUserID = r.getParameter("modifyID");
        Integer editID = Integer.parseInt(editUserID);

        // Parameters from gatherUserData form
        String FRM_login = r.getParameter("login");
        String FRM_email = r.getParameter("email");
        String FRM_name = r.getParameter("name");
        String FRM_passwd_A = r.getParameter("fPasswdA");
        String FRM_passwd_B = r.getParameter("fPasswdB");

        // Rejestracja drivera JDBC przed połączeniem do bazy
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        User userFromSQL = null;
        UserDao uDAO = new UserDao(dbName, dbTable);

        if(FRM_login!=null && uDAO.validateNewLogin(FRM_login)) {
            uDAO.update(editID, "login", FRM_login);
        }

        if(FRM_email!=null && uDAO.validateNewEmail(FRM_email)) {
            uDAO.update(editID, "email", FRM_email);
        }

        if(FRM_name!=null) {
            uDAO.update(editID, "name", FRM_name);
        }

        R.sendRedirect("/user/list");
    }

    protected void doGet(
            HttpServletRequest r, HttpServletResponse R)
            throws ServletException, IOException {

        final String dbName = "workshop3", dbTable = "users";
        String getUserID = r.getParameter("uid");
        Integer editID = Integer.parseInt(getUserID);

        // Rejestracja drivera JDBC przed połączeniem do bazy
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        User userFromSQL = null;
        UserDao uDAO = new UserDao(dbName, dbTable);

        if(editID!=null) {
            userFromSQL = uDAO.read(editID);
            r.setAttribute("PLH_login", userFromSQL.getLogin());
            r.setAttribute("PLH_email", userFromSQL.getEmail());
            r.setAttribute("PLH_name", userFromSQL.getName());
            r.setAttribute("PLH_passwdA", "password");
            r.setAttribute("PLH_passwdB", "re-type password");
            r.setAttribute("editID", editID);
        }

        r.setAttribute("LST_login", "");
        r.setAttribute("LST_email", "");
        r.setAttribute("LST_name", "");

        r.setAttribute("formInfo",
                "<p>Modify user Form</p><p>Fill the fields to modify</p>");
        r.setAttribute("action", "/user/edit");
        r.setAttribute("star", " ");
        getServletContext().getRequestDispatcher("/WEB-INF/gatherUserData.jsp")
                .forward(r, R);
    }
}
