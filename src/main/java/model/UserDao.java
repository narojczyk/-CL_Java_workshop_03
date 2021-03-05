package model;

import ctrl.User;
import utils.MyDBTools;

import java.sql.*;
import java.util.Arrays;

public class UserDao {

    private String SQLdataBase = "";
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?);";
    private static final String READ_USER_QUERY =
            "SELECT id,email,username FROM users WHERE id = ?;";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ? ;";
    private static final String GET_SIZE_QUERY = "SELECT COUNT(*) FROM users;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ? ;";
    private static final String SHOW_ALL_QUERY = "SELECT * FROM users";

    public UserDao(String SQLdataBase){
        this.SQLdataBase = SQLdataBase;
    }

    public void printAllDBase(){
       try(Connection c  = MyDBTools.mySQLConnect(SQLdataBase)){
            MyDBTools.printData(c, SHOW_ALL_QUERY,
                    "id","email","username", "password");
       }catch (SQLException e) {
           e.printStackTrace();
       }
    }
/*
    public int delete(int userID) {
        User ifExists = this.read(userID);
        if(userID > 0 && ifExists != null) {
            try(Connection c = MyDBTools.mySQLConnect(SQLdataBase);
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

    public User read(int userID){
        if(userID > 0) {
            try(Connection c = MyDBTools.mySQLConnect(SQLdataBase);
                PreparedStatement stmt = c.prepareStatement(READ_USER_QUERY);
            ) {
                stmt.setInt(1, userID ) ;
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    User userRead = new User();
                   userRead.setId(rs.getInt("id"));
                   userRead.setUsrEmail(rs.getString("email"));
                   userRead.setUsrName(rs.getString("username"));
                    return userRead;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int update(User user){
        if(user != null && user.getId() > 0){
            try(Connection c = MyDBTools.mySQLConnect(SQLdataBase);
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

    public long create(User userToAdd){
        if(userToAdd != null) {
            try(Connection c = MyDBTools.mySQLConnect(SQLdataBase);
                PreparedStatement stmt =
                        c.prepareStatement(CREATE_USER_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            ) {
                stmt.setString(1, userToAdd.getUsrName());
                stmt.setString(2, userToAdd.getUsrEmail());
                stmt.setString(3, hashPassword(userToAdd.getUsrPasswd()));
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            } catch (SQLException e) {
                    e.printStackTrace();
            }
        }
        return 0;
    }

    private String hashPassword(String password) {
        return BC.BCrypt.hashpw(password, BC.BCrypt.gensalt());
    }

    private int getDBaseSize(){
        try(Connection c = MyDBTools.mySQLConnect(SQLdataBase);
                PreparedStatement stmt = c.prepareStatement(GET_SIZE_QUERY);
            ) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
