package DAO;

import database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides logic for the Division DAO.
 *
 * @author Sophie Dang.
 */
public class DivisionDAO {
    /**
     * This method retrieves division data from the Division Table in the database.
     * @param country country string.
     * @return -> list of divisions.
     */
    public ObservableList<Division> retrieveDivisionDataFromDatabase(String country) {
        try {
            String sqlQueryString = "select first_level_divisions.Division_ID, countries.Country_ID, first_level_divisions.Division  " +
                    "from countries, first_level_divisions where countries.Country = ? and countries.Country_ID = first_level_divisions.Country_ID;";

            Connection connection = JDBC.getConnection();
            ObservableList<Division> listOfDivisions = FXCollections.observableArrayList();
            PreparedStatement ps = connection.prepareStatement(sqlQueryString);
            ps.setString(1, country);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listOfDivisions.add(generateDivision(rs));
            }
            return listOfDivisions;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * This method generates a division object from the database to be used.
     * @param rs
     * @return -> a new division
     * @throws SQLException
     */
    private static Division generateDivision(ResultSet rs) throws SQLException {
        return new Division(
                rs.getInt("Division_ID"),
                rs.getInt("Country_ID"),
                rs.getString("Division"));
    }
}
