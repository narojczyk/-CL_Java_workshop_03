package model;

import ctrl.User;

import java.sql.*;
import java.util.Arrays;

import static org.mindrot.jbcrypt.BCrypt.gensalt;
import static org.mindrot.jbcrypt.BCrypt.hashpw;
import static utils.MyDBTools.mySQLConnect;
import static utils.MyDBTools.printData;

public class UserDao {

    private String SQLdataBase;
    private String SQLtable;
    private static final String CREATE_USER_QUERY =
            "INSERT INTO _SQL-TABLE-NAME_(login, name, email, passwd) VALUES (?, ?, ?, ?);";
    private static final String READ_USER_QUERY =
            "SELECT id, login, email, name, passwd FROM _SQL-TABLE-NAME_ WHERE id = ?;";
    private static final String GET_SIZE_QUERY = "SELECT COUNT(*) FROM _SQL-TABLE-NAME_;";
    /*
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ? ;";

    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ? ;";*/
//    private static final String SHOW_ALL_QUERY = "SELECT * FROM users";
    private static final String CREATE_TABLE =
            "CREATE TABLE _SQL-TABLE-NAME_  ( " +
            "id INT AUTO_INCREMENT,"+
            "login VARCHAR(16) NOT NULL UNIQUE,"+
            "name VARCHAR(255),"+
            "email VARCHAR(32) NOT NULL UNIQUE,"+
            "passwd VARCHAR(65) NOT NULL,"+
            "PRIMARY KEY(id) );";

    public UserDao(String SQLdataBase, String SQLtable){
        this.SQLdataBase = SQLdataBase;
        this.SQLtable = SQLtable;
    }

    public User read(int userID){
        if(userID > 0) {
            try(Connection c = mySQLConnect(SQLdataBase);
                PreparedStatement stmt = c.prepareStatement(
                        READ_USER_QUERY.replaceAll("_SQL-TABLE-NAME_", SQLtable)) ) {
                stmt.setInt(1, userID ) ;
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
        return 0;
    }

    private String hashPassword(String password) {
        return hashpw(password, gensalt());
    }

    /*public void printAllDBase(){
       try(Connection c  = mySQLConnect(SQLdataBase)){
            printData(c, SHOW_ALL_QUERY,
                    "id","login", "email","name", "passwd");
       }catch (SQLException e) {
           e.printStackTrace();
       }
    }*/

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
    public int delete(int userID) {
        User ifExists = this.read(userID);
        if(userID > 0 && ifExists != null) {
            try(Connection c = mySQLConnect(SQLdataBase);
                PreparedStatement stmt = c.prepareStatement(DELETE_USER_QUERY);
            ) {
                stmt.setInt(1, userID ) ;
                return stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
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



    public int update(User user){
        if(user != null && user.getId() > 0){
            try(Connection c = mySQLConnect(SQLdataBase);
                PreparedStatement stmt =
                        c.prepareStatement(UPDATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            ) {
                stmt.setString(1, user.getUsrName());
                stmt.setString(2, user.getUsrEmail());
                stmt.setString(3, hashPassword(user.getUsrPasswd()));
                stmt.setInt(4, user.getId());
                return stmt.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException ecv){
                System.out.printf("Nieudana operacja - email '%s' juz istnieje\n",user.getUsrEmail());
                return 0;
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }




    
    private User[] extendUsersArray(User[] users, User newUser) {
        User[] extendedUsers = Arrays.copyOf(users, users.length + 1); 
        extendedUsers[users.length] = newUser; 
        return extendedUsers;
    }


     */
}
