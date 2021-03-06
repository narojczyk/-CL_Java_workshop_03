package view;

import ctrl.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user/delete")
public class UserDelete extends HttpServlet {
    protected void doPost(
            HttpServletRequest r, HttpServletResponse R)
            throws ServletException, IOException {

        final String dbName = "workshop3", dbTable = "users";
        String getUserID = r.getParameter("uid");
        Integer deleteID = Integer.parseInt(getUserID);
        R.getWriter().printf("uid %s received in post mode for delete \n",getUserID);

        if(deleteID > 0){
            // Rejestracja drivera JDBC przed połączeniem do bazy
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            UserDao uDAO = new UserDao(dbName, dbTable);
            uDAO.delete(deleteID);
        }
        R.sendRedirect("/user/list");
    }

    protected void doGet(
            HttpServletRequest r, HttpServletResponse R)
            throws ServletException, IOException {

        final String dbName = "workshop3", dbTable = "users";
        String getUserID = r.getParameter("uid");
        Integer deleteID = Integer.parseInt(getUserID);

        if(deleteID!=null && deleteID > 0){
            // Rejestracja drivera JDBC przed połączeniem do bazy
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            UserDao uDAO = new UserDao(dbName, dbTable);
            User userFromSQL = uDAO.read(deleteID);
            if(userFromSQL!=null) {
                r.setAttribute("UserData", userFromSQL);
            }
            r.setAttribute("action", "/user/delete");
            r.setAttribute("method", "post");
            r.setAttribute("actionDesc", "Potwierdź usunięcie");

        getServletContext().getRequestDispatcher("/WEB-INF/usershow.jsp")
                .forward(r, R);
        }
    }
}
