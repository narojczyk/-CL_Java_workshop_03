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

@WebServlet("/user/add")
public class UserAdd extends HttpServlet {
    protected void doPost(
            HttpServletRequest r, HttpServletResponse R)
            throws ServletException, IOException {

        // Parameters from gatherUserData form
        String FRM_login = r.getParameter("login");
        String FRM_email = r.getParameter("email");
        String FRM_name = r.getParameter("name");
        if(FRM_name == null) FRM_name="";
        String FRM_passwd_A = r.getParameter("fPasswdA");
        String FRM_passwd_B = r.getParameter("fPasswdB");
        final String dbName = "workshop3", dbTable = "users";

        UserDao uDAO = new UserDao(dbName, dbTable);

        boolean loginIsValid = uDAO.validateNewLogin(FRM_login);
        boolean emailIsValid = uDAO.validateNewEmail(FRM_email);
        boolean passwdMathes = FRM_passwd_A.equals(FRM_passwd_B);
        boolean strongPasswd = uDAO.testPasswdStrength(FRM_passwd_A);

        long ID = 0;
        if(true && loginIsValid && emailIsValid && passwdMathes && strongPasswd) {
            ID = uDAO.create(new User(FRM_login, FRM_name, FRM_email, FRM_passwd_A));
        }

        if(ID == -666){
            System.out.println("[UserAdd] User NOT added - null passwd to create() ID="+ID);
        }else if (ID <= 0){
            System.out.printf("[UserAdd] User NOT added ID=%d tests: ",ID);
            System.out.printf("login %s;", loginIsValid);
            System.out.printf("email %s;", emailIsValid);
            System.out.printf("passwdMatch %s;", passwdMathes);
            System.out.printf("passwdStrong %s;\n", strongPasswd);

            // Reuse valid data
//            r.setAttribute("LST_name", FRM_name);
//            if(loginIsValid) {
//                r.setAttribute("LST_login", FRM_login);
//            }
//            if(emailIsValid) {
//                r.setAttribute("LST_email", FRM_email);
//            }
            R.sendRedirect("/user/add");
        }else{
            System.out.printf("[UserAdd] User added to %s with ID=%d\n", dbName, ID);
//            r.setAttribute("LST_login", "");
//            r.setAttribute("LST_email", "");
//            r.setAttribute("LST_name", "");
            R.sendRedirect("/user/list");
        }
    }

    protected void doGet(
            HttpServletRequest r, HttpServletResponse R)
            throws ServletException, IOException {
//        String LST_login = r.getParameter("LST_login");
//        String LST_email = r.getParameter("LST_email");
//        String LST_name  = r.getParameter("LST_name");
        r.setAttribute("PLH_login", "login");
        r.setAttribute("PLH_email", "email");
        r.setAttribute("PLH_name", "full name");
        r.setAttribute("PLH_passwdA", "password");
        r.setAttribute("PLH_passwdB", "re-type password");
        r.setAttribute("editID", "0");
        r.setAttribute("formInfo",
                "<p>Add user Form</p><p>required fields are marked with(*)</p>");
        r.setAttribute("action", "/user/add");
        r.setAttribute("star", "*");
        getServletContext().getRequestDispatcher("/WEB-INF/gatherUserData.jsp")
                .forward(r, R);
    }
}
