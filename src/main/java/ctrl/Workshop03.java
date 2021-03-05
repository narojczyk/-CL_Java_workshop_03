package ctrl;

import model.UserDao;

import javax.swing.text.StyledEditorKit;
import java.sql.SQLException;

import static utils.MyDBTools.checkIfExists;
import static utils.MyDBTools.createDataBase;

public class Workshop03 {
    public static void main(String[] args) {
        final String SQL_CON_ERR = "Error connecting to MySQL server";
        final String SQL_OP_DBC = "[main] SQL database '%s' %s\n";
        final String SQL_OP_TAC = "[main] SQL table '%s' in '%s' %s\n";
        final String CONF_CRT = "created";
        final String CONF_FOUND = "exists";
        final String dbName = "workshop3", dbTable = "users";

        UserDao uDAO = new UserDao(dbName, dbTable);

//        uDAO.printAllDBase();

        Boolean dbExists = checkIfExists(dbName);
        if(dbExists != null && !dbExists){
            createDataBase(dbName);
            System.out.printf(SQL_OP_DBC , dbName , CONF_CRT);
        }else if(dbExists != null){
            System.out.printf(SQL_OP_DBC , dbName , CONF_FOUND);
        }else{
            System.out.println(SQL_CON_ERR);
        }

        Boolean dbTableCreated =  uDAO.createTable();
        if(dbTableCreated != null && dbTableCreated){
            System.out.printf(SQL_OP_TAC, dbTable, dbName, CONF_CRT);
        }else if(dbTableCreated != null){
            System.out.printf(SQL_OP_TAC, dbTable, dbName, CONF_FOUND);
        }else{
            System.out.println(SQL_CON_ERR);
        }
    }
}
