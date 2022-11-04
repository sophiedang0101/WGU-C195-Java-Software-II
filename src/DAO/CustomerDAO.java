package DAO;

import database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides logic for the Customer DAO.
 *
 * @author Sophie Dang.
 */
public class CustomerDAO {

    /**
     * This method retrieves customers' data from the Customers Table in the database.
     *
     * @return a list of customers.
     */
    public static ObservableList<Customers> retrieveCustomerDataFromDatabase() {
        Connection connection;
        ObservableList<Customers> listOfCustomers = FXCollections.observableArrayList();

        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "select customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, countries.Country," +
                    " first_level_divisions.Division, customers.Division_ID, countries.Country_ID from customers inner join first_level_divisions " +
                    "on first_level_divisions.Division_ID = customers.Division_ID inner join countries on first_level_divisions.Country_ID = countries.Country_ID;";

            ResultSet rs = connection.createStatement().executeQuery(sqlQueryString);
            while (rs.next()) {
                Customers newCustomer = new Customers(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getString("Country"),
                        rs.getString("Division"),
                        rs.getInt("Division_ID"),
                        rs.getInt("Country_ID"));

                listOfCustomers.add(newCustomer);
            }
            return listOfCustomers;

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }


    /**
     * This method creates a list of customers from the database table to populate tables.
     *
     * @return --> list of customers.
     */
    public static ObservableList<Customers> createListOfCustomersFromDatabase() {
        Connection connection;
        ObservableList<Customers> listOfCustomers = FXCollections.observableArrayList();

        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "select Customer_ID, Customer_Name from customers;";
            ResultSet rs = connection.createStatement().executeQuery(sqlQueryString);

            while (rs.next()) {
                Customers newCustomer = new Customers(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"));
                listOfCustomers.add(newCustomer);
            }
            return listOfCustomers;

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }


    /**
     * This method allows the user to update customer data in the database.
     *
     * @param customerIDNumber customer ID.
     * @param customerName customer name.
     * @param customerAddress customer address.
     * @param customerPostalCode customer postal code.
     * @param customerPhoneNumber customer phone number.
     * @param divisionIDNumber division ID.
     * @return true if the customer was updated successfully, and false if the update was unsuccessful.
     * @throws SQLException
     */
    public static boolean updateSelectedCustomer(int customerIDNumber, String customerName, String customerAddress, String customerPostalCode,
                                                 String customerPhoneNumber, String divisionIDNumber) throws SQLException {
        Connection connection;
        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "update customers set Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Division_ID=? where Customer_ID=?";
            PreparedStatement ps = connection.prepareStatement(sqlQueryString);
            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPostalCode);
            ps.setString(4, customerPhoneNumber);
            ps.setInt(5, Integer.parseInt(divisionIDNumber));
            ps.setInt(6, customerIDNumber);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return false;
    }


    /**
     * This method allows the user to create new customer data and add it to the database.
     *
     * @param customerName customer name.
     * @param customerAddress customer address.
     * @param customerPostalCode customer postal code.
     * @param customerPhoneNumber customer phone number.
     * @param divisionIDNumber division ID.
     * @return
     * @throws SQLException
     */
    public static boolean createNewCustomer(String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, String divisionIDNumber) throws SQLException {
        Connection connection;
        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "insert into customers(Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sqlQueryString);
            statement.setString(1, customerName);
            statement.setString(2, customerAddress);
            statement.setString(3, customerPostalCode);
            statement.setString(4, customerPhoneNumber);
            statement.setInt(5, Integer.parseInt(divisionIDNumber));
            statement.execute();

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return false;
    }


    /**
     * This method lets the user delete customer data from the Customer Table in the database.
     *
     * @param customerIDNumber customer ID number.
     * @return true if the customer was deleted, and false if the customer wasn't deleted.
     * @throws SQLException
     */
    public static boolean deleteSelectedCustomer(int customerIDNumber) throws SQLException {
        Connection connection;
        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "delete from customers where Customer_ID = ?;";
            PreparedStatement ps = connection.prepareStatement(sqlQueryString);
            ps.setInt(1, customerIDNumber);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return false;
    }
}
