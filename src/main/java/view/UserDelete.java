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

@WebServlet("/user/delete")
public class UserDelete extends HttpServlet {
    protected void doPost(
            HttpServletRequest RX, HttpServletResponse TX)
            throws ServletException, IOException {

        String getUserID = RX.getParameter("uid");
        Integer deleteID = Integer.parseInt(getUserID);
        TX.getWriter().printf("uid %s received in post mode for delete \n",getUserID);

        if(deleteID > 0){
            UserDao uDAO = new UserDao(SQL_DATABASE_NAME, SQL_TABLE_NAME);
            uDAO.delete(deleteID);
        }
        TX.sendRedirect(SERVLET_CONTEXT+"/user/list");
    }

    protected void doGet(
            HttpServletRequest RX, HttpServletResponse TX)
            throws ServletException, IOException {

        String getUserID = RX.getParameter("uid");
        Integer deleteID = Integer.parseInt(getUserID);

        if(deleteID!=null && deleteID > 0){
            UserDao uDAO = new UserDao(SQL_DATABASE_NAME, SQL_TABLE_NAME);
            User userFromSQL = uDAO.read(deleteID);
            if(userFromSQL!=null) {
                RX.setAttribute("UserData", userFromSQL);
            }
            RX.setAttribute("ViewName", "Delete user");
            RX.setAttribute("mkRed"," pushbuttonRed");
            RX.setAttribute("action", SERVLET_CONTEXT+"/user/delete");
            RX.setAttribute("method", "post");
            RX.setAttribute("actionDesc", "Confirm delete");

        getServletContext().getRequestDispatcher("/WEB-INF/usershow.jsp")
                .forward(RX, TX);
        }
    }
}
