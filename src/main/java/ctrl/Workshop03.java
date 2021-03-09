package ctrl;

import model.UserDao;

import javax.sound.midi.Soundbank;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static ctrl.Parameters.*;
import static utils.MyDBTools.checkIfExists;
import static utils.MyDBTools.createDataBase;

public class Workshop03 {
    public static void main(String[] args) {
        final String SQL_CON_ERR = "Error connecting to MySQL server";
        final String SQL_OP_DBC = "[main] SQL database '%s' %s\n";
        final String SQL_OP_TAC = "[main] SQL table '%s' in '%s' %s\n";
        final String CONF_CRT = "created";
        final String CONF_FOUND = "exists";
        final String dbName = SQL_DATABASE_NAME, dbTable = SQL_TABLE_NAME;

        UserDao uDAO = new UserDao(dbName, dbTable);

        Set<User> DefaultUsers = new HashSet<>();

        // Check if database exists on the server and create one otherwise
        Boolean dbExists = checkIfExists(dbName);
        if(dbExists != null && !dbExists){
            createDataBase(dbName);
            System.out.printf(SQL_OP_DBC , dbName , CONF_CRT);
        }else if(dbExists != null){
            System.out.printf(SQL_OP_DBC , dbName , CONF_FOUND);
        }else{
            System.out.println(SQL_CON_ERR);
        }

        // Check if table exists in the database and create one otherwise
        Boolean dbTableCreated =  uDAO.createTable();
        if(dbTableCreated != null && dbTableCreated){
            System.out.printf(SQL_OP_TAC, dbTable, dbName, CONF_CRT);
        }else if(dbTableCreated != null){
            System.out.printf(SQL_OP_TAC, dbTable, dbName, CONF_FOUND);
        }else{
            System.out.println(SQL_CON_ERR);
        }

        // Populate data base with default data
        DefaultUsers = createDefUserSet(DefaultUsers);
        initiallyPopulateDB(uDAO, DefaultUsers);

        // test reading from SQL
        Map<Integer,User> UsersMap = uDAO.getUsersMap();

        User temp;
        int recordsPrinted=0, i=1;
        System.out.println("Loading " + uDAO.getRecordsCount() + " records from '"+dbTable+"'");
        while(recordsPrinted < uDAO.getRecordsCount()){
            temp = uDAO.read(i++);
            if(temp != null) {
                System.out.println(temp.toStringForTesting());
                recordsPrinted++;
            }
        }
    }

    public static void initiallyPopulateDB(UserDao udao, Set<User> users ){
        long id, added=0;
        final String MSG_FORMAT = "[initiallyPopulateDB] User%sadded %s\n";
        for(User usr : users){
            id = udao.create(usr);
            if(id == DAO_CREATE_FAILED_SQL_CONSTRAINT_VIOLATION){
                System.out.printf(MSG_FORMAT," not ", "- duplicate found");
            }else if (id == DAO_CREATE_FAILED){
                System.out.printf(MSG_FORMAT," not ", "- unknown reason");
            }else{
                System.out.printf(MSG_FORMAT," ","to '"+ SQL_TABLE_NAME +"' with ID="+id);
                added++;
            }
        }
        System.out.println("[initiallyPopulateDB] " + added + " records added '"+SQL_TABLE_NAME+"'");
    }

    public static Set<User> createDefUserSet(Set<User> users){
        users.add(new User("login1", "John W. Doe, the first",
                "terefere@gdzies.tam.pl", "passwd1"));
        users.add(new User("login2", "John W. Doe, the second",
                "jwdoe@gdzies.tam.pl", "passwd3"));
        users.add(new User("login3", "John W. Doe, the third",
                "jdoe3@gdzies.tam.pl", "passwd2"));
        users.add(new User("login4", "John S. Doe",
                "blabla@gdzies.tam.pl", "passwd4"));
        return users;
    }
}
