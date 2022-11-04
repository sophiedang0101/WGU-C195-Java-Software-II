package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC {
    private static PreparedStatement preparedStatement;
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static String password = "Passw0rd!";
    private static Connection connection = null;

    /**
     * This method opens / start the connection to the database.
     */
    public static void startConnection(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection successful!");
        }
        catch(ClassNotFoundException | SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * A getter for the database connection.
     * @return connection to the database.
     */
    public static Connection getConnection(){
        return connection;
    }

    /**
     * This method closes the connection to the database.
     */
    public static void closeConnection(){
        try{
            connection.close();
            System.out.println("Connection closed!");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void setPreparedStatement(Connection conn, String sqlQuery) throws SQLException {
        if(conn != null)
            preparedStatement = conn.prepareStatement(sqlQuery);
        else
            System.out.println("Error!There was a problem creating the prepared statement.");
    }

    public static PreparedStatement getPreparedStatement(){
        if(preparedStatement != null)
            return preparedStatement;
        else
            System.out.println("Null reference to Prepared Statement");
        return null;
    }
}
