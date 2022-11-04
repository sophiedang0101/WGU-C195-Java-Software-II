package DAO;

import database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides logic for the User DAO.
 *
 * @author Sophie Dang.
 */
public class UserDAO {

    /**
     * This method retrieves the user's data from the Users table in the database.
     * @return a list of users from the database.
     * @throws SQLException
     */
    public static ObservableList<User> createListOfUsersFromDatabase() {
        Connection connection;
        ObservableList<User> usersList = FXCollections.observableArrayList();

        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "select User_ID, User_Name from users;";
            ResultSet rs = connection.createStatement().executeQuery(sqlQueryString);

            while (rs.next()) {
                User newUser = new User(
                        rs.getInt("User_ID"),
                        rs.getString("User_Name"));
                usersList.add(newUser);
            }
            return usersList;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
