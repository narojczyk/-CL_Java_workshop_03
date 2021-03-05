package utils;

import java.sql.*;

public class MyDBTools{
    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "bnmmnb";
    private static final String DB_PAR_ENC = "characterEncoding=utf8";
    private static final String DB_PAR_SSL = "useSSL=false";
    private static final String DB_EXISTS_QUERY = "SHOW DATABASES LIKE ?;";
    private static final String DB_CREATE =
            "CREATE DATABASE IF NOT EXISTS _SQL-DBASE-NAME_"+
            " CHARACTER SET utf8mb4 " +
            "COLLATE utf8mb4_unicode_ci;";

    public static Connection mySQLServer() throws SQLException {
        String DB_PARAMS = "?" + DB_PAR_ENC + "&" + DB_PAR_SSL;
        String url = DB_URL + DB_PARAMS;
        // Przygotowanie połączenia do servera MySQL (bez wybierania bazy)
        return DriverManager.getConnection(url, DB_USER, DB_PASS);
    }

    public static Connection mySQLConnect(String database) throws SQLException {
        String DB_PARAMS = "?" + DB_PAR_ENC + "&" + DB_PAR_SSL;
        String url = DB_URL + (database != null ? "/" + database : "") + DB_PARAMS;
        return DriverManager.getConnection(url, DB_USER, DB_PASS);
    }

    public static Boolean createDataBase(String database) {
        try(Connection c = mySQLServer()){
           // Przygotowanie zapytania do utworzenia 'database'
           PreparedStatement stmt = c.prepareStatement(
                   DB_CREATE.replaceAll("_SQL-DBASE-NAME_",database));
           stmt.executeUpdate();
           return true;
        }catch (SQLException e) {
            System.out.println("ERROR while creating database");
            e.printStackTrace();
            return false;
        }
    }

    // Returns true is 'database' exists on the server
    public static Boolean checkIfExists(String database){
       try( Connection c = mySQLServer()) {
           // Przygotowanie zapytania do MySQL o 'database'
           PreparedStatement stmt = c.prepareStatement(DB_EXISTS_QUERY);
           // Zastąpienie '?' w DB_EXISTS_QUERY przez zawartość 'database'
           stmt.setString(1, database);
           ResultSet rs = stmt.executeQuery();
           if(!rs.next()){
               return false;
           }else {
               return true;
           }
       }catch (SQLException e) {
            System.out.println("ERROR while creating database");
            e.printStackTrace();
       }
       return null;
    }

    public static void printData(Connection c, String q, String... columnNames) throws SQLException {
        for (String param : columnNames) {
            System.out.printf("| %s ", param);
        }
        System.out.println("|");
        try (
                PreparedStatement statement = c.prepareStatement(q);
                ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                for (String param : columnNames) {
                    System.out.printf("| %s ", resultSet.getString(param));
                }
                System.out.println("|");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
