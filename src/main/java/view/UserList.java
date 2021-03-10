package view;

import ctrl.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static ctrl.Parameters.*;


@WebServlet("/user/list")
public class UserList extends HttpServlet {
 /*   protected void doPost(
            HttpServletRequest r, HttpServletResponse R)
            throws ServletException, IOException {
    }*/

    protected void doGet(
            HttpServletRequest RX, HttpServletResponse TX)
            throws ServletException, IOException {

        UserDao uDAO = new UserDao(SQL_DATABASE_NAME, SQL_TABLE_NAME);

        Map<Integer,User> UsersMap =  uDAO.getUsersMap();

        RX.setAttribute("SRV_CON", SERVLET_CONTEXT);
        RX.setAttribute("ViewName", "Registered users");
        RX.setAttribute("UsersMap", UsersMap);

        getServletContext().getRequestDispatcher(
                "/WEB-INF/userlist.jsp").forward(RX, TX);

    }
}
