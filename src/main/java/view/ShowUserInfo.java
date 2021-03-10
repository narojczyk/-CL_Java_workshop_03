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


@WebServlet("/user/show")
public class ShowUserInfo extends HttpServlet {
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String name = request.getParameter("modifyID");
//        response.getWriter().printf("got %s by POST\n", name);
//
//    }

    protected void doGet(
            HttpServletRequest RX, HttpServletResponse TX)
            throws ServletException, IOException {

        String getUserID = RX.getParameter("uid");
        Integer showID = Integer.parseInt(getUserID);

        UserDao uDAO = new UserDao(SQL_DATABASE_NAME, SQL_TABLE_NAME);

        if(showID!=null) {
            User userFromSQL = uDAO.read(showID);
            if(userFromSQL!=null) {
                RX.setAttribute("UserData", userFromSQL);
            }
        }else{
            RX.setAttribute("UserData", "");
        }

        RX.setAttribute("ViewName", "User details");
        RX.setAttribute("mkRed","");
        RX.setAttribute("action", SERVLET_CONTEXT+"/user/edit");
        RX.setAttribute("method", "get");
        RX.setAttribute("actionDesc", "Modify user data");

        getServletContext().getRequestDispatcher("/WEB-INF/usershow.jsp")
                .forward(RX, TX);

    }
}
