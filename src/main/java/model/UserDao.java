package model;

import ctrl.User;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import static org.mindrot.jbcrypt.BCrypt.gensalt;
import static org.mindrot.jbcrypt.BCrypt.hashpw;
import static utils.MyDBTools.mySQLConnect;

public class UserDao {

    private String SQLdataBase;
    private String SQLtable;
    private static final int size_login  = 16;
    private static final int size_name   = 255;
    private static final int size_email  = 32;
    private static final int size_passwd = 65;
    private static final int size_passwd_min_chars = 8;
    private static final String CREATE_USER_QUERY =
            "INSERT INTO _SQL-TABLE-NAME_(login, name, email, passwd) VALUES (?, ?, ?, ?);";
    private static final String READ_USER_QUERY =
            "SELECT id, login, email, name, passwd FROM _SQL-TABLE-NAME_ WHERE id = ?;";
    private static final String GET_SIZE_QUERY = "SELECT COUNT(*) FROM _SQL-TABLE-NAME_;";
    private static final String GET_VAR_COUNT =
            "SELECT COUNT(_SQL-COLUMN-NAME_) FROM _SQL-TABLE-NAME_ "+
            "WHERE _SQL-COLUMN-NAME_ LIKE \"_SQL-SEARCH-FOR_\";";
    private static final String UPDATE_USER_QUERY =
            "UPDATE _SQL-TABLE-NAME_ SET _SQL-COLUMN-NAME_ = ? WHERE id = ? ;";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM _SQL-TABLE-NAME_ WHERE id = ? ;";
    private static final String ID_USER_QUERY =
            "SELECT id FROM _SQL-TABLE-NAME_ ORDER BY id ASC ;";
    private static final String CREATE_TABLE =
            "CREATE TABLE _SQL-TABLE-NAME_  ( " +
            "id INT AUTO_INCREMENT,"+
            "login VARCHAR("+ size_login +") NOT NULL UNIQUE,"+
            "name VARCHAR("+ size_name +"),"+
            "email VARCHAR("+ size_email +") NOT NULL UNIQUE,"+
            "passwd VARCHAR("+ size_passwd +") NOT NULL,"+
            "PRIMARY KEY(id) );";

    public UserDao(String SQLdataBase, String SQLtable){
        this.SQLdataBase = SQLdataBase;
        this.SQLtable = SQLtable;
    }

    public Map<Integer,User> getUsersMap(){
        Map<Integer,User> UsersMap = new HashMap<>();
        long[] IDs = getRecordIDs();
        for(int i=0; i<IDs.length; i++){
            UsersMap.put((int) IDs[i], read(IDs[i]));
        }
        return UsersMap;
    }

    private long[] getRecordIDs() {
        long[] recordIDs = new long[0];
        int currentLength = recordIDs.length;

        try(Connection c = mySQLConnect(SQLdataBase);
            PreparedStatement stmt = c.prepareStatement(
                    ID_USER_QUERY.replaceAll("_SQL-TABLE-NAME_", SQLtable)) ) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                recordIDs = Arrays.copyOf(recordIDs, currentLength + 1);
                recordIDs[currentLength++] = rs.getLong("id");
            }
            return recordIDs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordIDs;
    }

    public int delete(int userID) {
        User ifExists = this.read(userID);
        if(userID > 0 && ifExists != null) {
            try(Connection c = mySQLConnect(SQLdataBase);
                PreparedStatement stmt = c.prepareStatement(
                        DELETE_USER_QUERY.replaceAll("_SQL-TABLE-NAME_", SQLtable)) ) {
                stmt.setInt(1, userID ) ;
                return stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int update(long ID, String collumn, String newValue){
        if(ID > 0){
            try(Connection c = mySQLConnect(SQLdataBase);
                PreparedStatement stmt =
                        c.prepareStatement(
                                UPDATE_USER_QUERY.
                                        replaceAll("_SQL-TABLE-NAME_", SQLtable).
                                        replaceAll("_SQL-COLUMN-NAME_", collumn),
                                PreparedStatement.RETURN_GENERATED_KEYS);
            ) {
                stmt.setString(1, newValue);
                stmt.setLong(2,ID);
                return stmt.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException ecv){
                System.out.printf("[UserDAO update] Failed - %s='%s' exists\n",
                        collumn, newValue);
                return 0;
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public User read(long userID){
        if(userID > 0) {
            try(Connection c = mySQLConnect(SQLdataBase);
                PreparedStatement stmt = c.prepareStatement(
                        READ_USER_QUERY.replaceAll("_SQL-TABLE-NAME_", SQLtable)) ) {
                stmt.setLong(1, userID ) ;
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    User userFromSQL = new User();
                    userFromSQL.setID(rs.getInt("id"));
                    userFromSQL.setLogin(rs.getString("login"));
                    userFromSQL.setEmail(rs.getString("email"));
                    userFromSQL.setName(rs.getString("name"));
                    userFromSQL.setPasswd(rs.getString("passwd"));
                    return userFromSQL;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public long create(User userToAdd){
        if(userToAdd != null) {
            try(Connection c = mySQLConnect(SQLdataBase);
                PreparedStatement stmt =
                        c.prepareStatement(CREATE_USER_QUERY.replaceAll("_SQL-TABLE-NAME_", SQLtable),
                                PreparedStatement.RETURN_GENERATED_KEYS) ) {
                stmt.setString(1, userToAdd.getLogin());
                stmt.setString(2, userToAdd.getName());
                stmt.setString(3, userToAdd.getEmail());
                stmt.setString(4, hashPassword(userToAdd.getPasswd()));
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }catch (SQLIntegrityConstraintViolationException e){
                return -99;
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -666;
    }

    private String hashPassword(String password) {
        return hashpw(password, gensalt());
    }

    public int getRecordsCount(){
        try(Connection c = mySQLConnect(SQLdataBase);
            PreparedStatement stmt = c.prepareStatement(
                    GET_SIZE_QUERY.replaceAll("_SQL-TABLE-NAME_", SQLtable)) ) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Boolean createTable(){
        Boolean tabExists = false;
       try(Connection c  = mySQLConnect(SQLdataBase)){
           // Pobierz meda dane z servera SQL
           DatabaseMetaData meta = c.getMetaData();
           // Przeszukaj meta dane w poszukiwaniu nazwy tabeli
           ResultSet res = meta.getTables(
                   null, null, SQLtable,  new String[] {"TABLE"});
           // res zawiera liste baz danych w której znaleziono tabele o podanej nazwie,
           // sprawdzic czy aktualna baza jest na liscie
           while (res.next()){
               if(res.getString("TABLE_CAT").equals(SQLdataBase)){
                   tabExists = true;
               }
           }
           // jezeli nie znalazl na liscie: stworzyc tabele
           if(!tabExists) {
               PreparedStatement stmt = c.prepareStatement(
                       CREATE_TABLE.replaceAll("_SQL-TABLE-NAME_", SQLtable));
               stmt.executeUpdate();
               return true;
           }else{
               return false;
           }
       }catch (SQLException e) {
           e.printStackTrace();
           return null;
       }
    }

/*

/*
    public User[] readAll(){
        int trialId=1;
        User[] allUsers = new User[0];
        User current;
        for(int i=1; i<=getDBaseSize(); i++){
            do{
                current = this.read(trialId++);
                if(current != null){
                    allUsers = extendUsersArray(allUsers, current);
                }
            }while(current != null);
        }
        return allUsers;
    }

    private User[] extendUsersArray(User[] users, User newUser) {
        User[] extendedUsers = Arrays.copyOf(users, users.length + 1); 
        extendedUsers[users.length] = newUser; 
        return extendedUsers;
    }


     */

    private int countMatching(String collumn, String searchValue){
        try(Connection c = mySQLConnect(SQLdataBase);
            PreparedStatement stmt = c.prepareStatement(
                    GET_VAR_COUNT.
                            replaceAll("_SQL-TABLE-NAME_", SQLtable).
                            replaceAll("_SQL-COLUMN-NAME_", collumn).
                            replaceAll("_SQL-SEARCH-FOR_", searchValue) )) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean validateNewLogin(String candidateLogin){
        if (candidateLogin!=null) {
            boolean loginLength = candidateLogin.length() < size_login;
            boolean loginUsed = countMatching("login", candidateLogin) == 0;
            Pattern regex1 = Pattern.compile("^[^0-9][a-zA-Z0-9_]{4," + size_login + "}");
            boolean allowedChars = regex1.matcher(candidateLogin).matches();
            return true && loginLength && loginUsed && allowedChars;
        }
        return false;
    }

    public boolean validateNewEmail(String candidateEmail){
        final String EMAIL_REGEX = "[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.([a-zA-Z]{2,}){1}";
        if(candidateEmail!=null) {
            boolean emailLength = candidateEmail.length() < size_email;
            boolean emailUsed = countMatching("email", candidateEmail) == 0;
            Pattern regex1 = Pattern.compile(EMAIL_REGEX);
            boolean validEmailForm = regex1.matcher(candidateEmail).matches();
            return true && emailLength && emailUsed && validEmailForm;
        }
        return false;
    }

    public boolean testPasswdStrength(String candidatePasswd){
        Pattern regex1 = Pattern.compile("[\\s\\W]+");
        Pattern regex2 = Pattern.compile("[a-z]+");
        Pattern regex3 = Pattern.compile("[A-Z]+");
        Pattern regex4 = Pattern.compile("[\\d]+");
        if(candidatePasswd!=null) {
            boolean passwdLength = candidatePasswd.length() > size_passwd_min_chars;
            boolean strongPasswd1 = regex1.matcher(candidatePasswd).find();
            boolean strongPasswd2 = regex2.matcher(candidatePasswd).find();
            boolean strongPasswd3 = regex3.matcher(candidatePasswd).find();
            boolean strongPasswd4 = regex4.matcher(candidatePasswd).find();
            return true && passwdLength &&
                    strongPasswd1 && strongPasswd2 && strongPasswd3 && strongPasswd4;
        }
        return false;
    }
}
