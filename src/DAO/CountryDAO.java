package DAO;

import database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * This class provides logic for the Country DAO.
 *
 * @author Sophie Dang.
 */
public class CountryDAO {
    /**
     * This method gets country data from the Country table in the database.
     * @return -> list of countries.
     */
    public static ObservableList<Country> retrieveCountryDataFromDatabase() {
        Connection connection;
        ObservableList<Country> countriesList = FXCollections.observableArrayList();
        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "select * from countries";
            ResultSet rs = connection.createStatement().executeQuery(sqlQueryString);

            while (rs.next()) {
                Country newCountry = new Country(
                        rs.getInt("Country_ID"),
                        rs.getString("Country"));
                countriesList.add(newCountry);
            }
            return countriesList;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
