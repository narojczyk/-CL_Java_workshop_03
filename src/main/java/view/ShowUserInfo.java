package view;

import ctrl.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user/show")
public class ShowUserInfo extends HttpServlet {
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String name = request.getParameter("modifyID");
//        response.getWriter().printf("got %s by POST\n", name);
//
//    }

    protected void doGet(
            HttpServletRequest r, HttpServletResponse R)
            throws ServletException, IOException {

        final String dbName = "workshop3", dbTable = "users";
        String getUserID = r.getParameter("uid");
        Integer showID = Integer.parseInt(getUserID);

        // Rejestracja drivera JDBC przed połączeniem do bazy
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        User userFromSQL = null;
        UserDao uDAO = new UserDao(dbName, dbTable);

        if(showID!=null) {
            userFromSQL = uDAO.read(showID);
        }
        if(userFromSQL!=null) {
            r.setAttribute("UserData", userFromSQL);
        }

        getServletContext().getRequestDispatcher("/WEB-INF/usershow.jsp")
                .forward(r, R);

    }
}
