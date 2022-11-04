package DAO;

import database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contacts;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * This class provides logic for the Contact DAO.
 *
 * @author Sophie Dang.
 */
public class ContactDAO {
    /**
     * This method retrieves contact data from the Contacts table in the database.
     * @return --> list of contacts.
     */
    public static ObservableList<Contacts> retrieveContactDataFromDatabase() {
        Connection conn;
        ObservableList<Contacts> contactsList = FXCollections.observableArrayList();

        try {
            conn = JDBC.getConnection();
            String sqlQueryString = "select * from contacts;";
            ResultSet rs = conn.createStatement().executeQuery(sqlQueryString);
            while (rs.next()) {
                Contacts newContact = new Contacts(
                        rs.getInt("Contact_ID"),
                        rs.getString("Contact_Name"),
                        rs.getString("Email"));
                contactsList.add(newContact);
            }
            return contactsList;
        }
        catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }
}
