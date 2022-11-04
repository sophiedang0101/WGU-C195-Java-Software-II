package DAO;

import database.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * This class provides logic for the Appointment DAO.
 *
 * @author Sophie Dang
 */
public class AppointmentDAO {
    /**
     * This method retrieves the data for the main Appointments Table View from the Appointments table in the database.
     *
     * @return --> list of appointments.
     */
    public static ObservableList<Appointments> retrieveAppointmentDataFromDatabase() {
        Connection connection;
        ObservableList<Appointments> listOfAppointments = FXCollections.observableArrayList();
        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "select appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, contacts.Contact_Name, " +
                    "appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.User_ID, appointments.Contact_ID from appointments, " +
                    "contacts where appointments.Contact_ID = contacts.Contact_ID;";
            ResultSet rs = connection.createStatement().executeQuery(sqlQueryString);
            while (rs.next()) {
                Appointments newAppointments = new Appointments(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Contact_Name"),
                        rs.getString("Type"),
                        rs.getDate("Start").toLocalDate(),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getDate("End").toLocalDate(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));

                listOfAppointments.add(newAppointments);
            }
            return listOfAppointments;

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * This method creates a new Appointment object from the database.
     *
     * @param rs
     * @return --> return a new Appointment object.
     * @throws SQLException
     */
    private static Appointments newAppointments(ResultSet rs) throws SQLException {
        return new Appointments(
                rs.getInt("Appointment_ID"),
                rs.getString("Title"),
                rs.getString("Description"),
                rs.getString("Location"),
                rs.getString("Contact_Name"),
                rs.getString("Type"),
                rs.getDate("Start").toLocalDate(),
                rs.getTimestamp("Start").toLocalDateTime(),
                rs.getDate("End").toLocalDate(),
                rs.getTimestamp("End").toLocalDateTime(),
                rs.getInt("Customer_ID"),
                rs.getInt("User_ID"),
                rs.getInt("Contact_ID"));
    }


    /**
     * This method gets the data for the Appointments Table View based on the current week.
     *
     * @return --> list of appointments for the current week.
     */
    public static ObservableList<Appointments> weeklyAppointmentsList() {
        Connection connection;
        ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();

        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "select appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location," +
                    " contacts.Contact_Name, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.User_ID," +
                    " appointments.Contact_ID from appointments inner join contacts on appointments.Contact_ID = contacts.Contact_ID where week(Start)=week(now());";

            ResultSet rs = connection.createStatement().executeQuery(sqlQueryString);
            while (rs.next()) {
                Appointments newAppointments = new Appointments(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Contact_Name"),
                        rs.getString("Type"),
                        rs.getDate("Start").toLocalDate(),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getDate("End").toLocalDate(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));
                appointmentsList.add(newAppointments);
            }
            return appointmentsList;

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }


    /**
     * This method gets the data for the main Appointments Table View based on the current month.
     *
     * @return --> list of appointments based on the current month.
     */
    public static ObservableList<Appointments> monthlyAppointmentsList() {
        Connection connection;
        ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();

        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "select appointments.Appointment_ID, appointments.Title, appointments.Description, appointments.Location, " +
                    "contacts.Contact_Name, appointments.Type, appointments.Start, appointments.End, appointments.Customer_ID, appointments.User_ID, " +
                    "appointments.Contact_ID from appointments inner join contacts on appointments.Contact_ID = contacts.Contact_ID where month(Start)=month(now());";

            ResultSet rs = connection.createStatement().executeQuery(sqlQueryString);
            while (rs.next()) {
                Appointments newAppointments = new Appointments(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Contact_Name"),
                        rs.getString("Type"),
                        rs.getDate("Start").toLocalDate(),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getDate("End").toLocalDate(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID"));

                appointmentsList.add(newAppointments);
            }
            return appointmentsList;

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }


    /**
     * This method sets up alerts for upcoming appointments that are within 15 minutes of the user's current time.
     *
     * @return alerts when there are incoming appts within 15 mins.
     */
    public static ObservableList<Appointments> appointmentWithin15MinsAlerts() {
        Connection connection;
        ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();

        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "select Appointment_ID, Start from appointments a where a.start >= now() and a.start <= date_add(now(), interval 15 minute);";

            ResultSet rs = connection.createStatement().executeQuery(sqlQueryString);
            while (rs.next()) {
                Appointments newAppointments = new Appointments(
                        rs.getInt("Appointment_ID"),
                        rs.getDate("Start").toLocalDate(),
                        rs.getTimestamp("Start").toLocalDateTime());

                appointmentsList.add(newAppointments);
            }
            return appointmentsList;

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return null;
    }


    /**
     * This method lets the user update the appointment's data in the database.
     *
     * @param apptIDNumber appointment ID.
     * @param appointmentTitle appointment title.
     * @param appointmentDescription appointment description.
     * @param appointmentLocation appointment location.
     * @param contactName contact name.
     * @param appointmentType appointment type.
     * @param appointmentStart appointment start.
     * @param appointmentEnd appointment end.
     * @param customerIDNumber customer ID.
     * @param userIDNumber user ID.
     * @return
     * @throws SQLException
     */
    public static boolean updateSelectedAppointment(int apptIDNumber, String appointmentTitle, String appointmentDescription, String appointmentLocation, int contactName, String appointmentType,
                                                    LocalDateTime appointmentStart, LocalDateTime appointmentEnd, int customerIDNumber, int userIDNumber) throws SQLException {
        Connection connection;
        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "update appointments set Title = ?, Description = ?, Location = ?, Contact_ID = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ? " +
                    "where Appointment_ID = ?;";

            PreparedStatement ps = connection.prepareStatement(sqlQueryString);
            ps.setString(1, appointmentTitle);
            ps.setString(2, appointmentDescription);
            ps.setString(3, appointmentLocation);
            ps.setInt(4, contactName);
            ps.setString(5, appointmentType);
            ps.setTimestamp(6, Timestamp.valueOf(appointmentStart));
            ps.setTimestamp(7, Timestamp.valueOf(appointmentEnd));
            ps.setInt(8, customerIDNumber);
            ps.setInt(9, userIDNumber);
            ps.setInt(10, apptIDNumber);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }

        return false;
    }


    /**
     * This method lets the user create new appointment data for the database.
     *
     * @param appointmentTitle appointment title.
     * @param appointmentDescription appointment description.
     * @param appointmentLocation appointment location.
     * @param contactName contact name.
     * @param appointmentType appointment type.
     * @param appointmentStart appointment start.
     * @param appointmentEnd appointment end.
     * @param customerIDNumber customer ID.
     * @param userIDNumber user ID.
     * @return
     * @throws SQLException
     */
    public static boolean createNewAppointment(String appointmentTitle, String appointmentDescription, String appointmentLocation, int contactName, String appointmentType,
                                               LocalDateTime appointmentStart, LocalDateTime appointmentEnd, int customerIDNumber, int userIDNumber) throws SQLException {
        Connection connection;
        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "insert into appointments(Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(sqlQueryString);
            ps.setString(1, appointmentTitle);
            ps.setString(2, appointmentDescription);
            ps.setString(3, appointmentLocation);
            ps.setInt(4, contactName);
            ps.setString(5, appointmentType);
            ps.setTimestamp(6, Timestamp.valueOf(appointmentStart));
            ps.setTimestamp(7, Timestamp.valueOf(appointmentEnd));
            ps.setInt(8, customerIDNumber);
            ps.setInt(9, userIDNumber);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return false;
    }


    /**
     * This method lets the user delete appointment data from the database.
     *
     * @param apptIDNumber appointment ID.
     * @return
     * @throws SQLException
     */
    public static boolean deleteSelectedAppointment(int apptIDNumber) throws SQLException {
        Connection connection;
        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "delete from appointments where Appointment_ID = ?;";
            PreparedStatement ps = connection.prepareStatement(sqlQueryString);
            ps.setInt(1, apptIDNumber);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return false;
    }

    /**This method deletes an appointment associated with a customer where the customer ID matches the appointment ID.
     *This method makes sure the associated appointment is also deleted when a customer is removed since in the virtual lab environment
     *the foreign key on delete option isn't set to "cascade".
     * @param apptIDNumber appointment ID number.
     */
    public static boolean deleteAppointmentByCustomer(int apptIDNumber) throws SQLException {
        Connection connection;
        try {
            connection = JDBC.getConnection();
            String sqlQueryString = "delete from appointments where Customer_ID = ?;";
            PreparedStatement ps = connection.prepareStatement(sqlQueryString);
            ps.setInt(1, apptIDNumber);
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
        return false;
    }
}
