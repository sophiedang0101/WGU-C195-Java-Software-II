package model;

import database.JDBC;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * This class is used to create appointments.
 *
 * @author Sophie Dang.
 */
public class Appointments {
    private ZonedDateTime localZonedDateTimeStart;
    private ZonedDateTime utcZonedDateTimeStart;
    private String utcAppointmentStartTimestamp;
    private ZonedDateTime localZonedDateTimeEnd;
    private ZonedDateTime utcZonedDateTimeEnd;
    private String utcAppointmentEndTimestamp;
    private int appointmentIDNumber;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String contactName;
    private String appointmentType;
    private LocalDate appointmentStartDate;
    private LocalDateTime appointmentStartTime;
    private LocalDate appointmentEndDate;
    private LocalDateTime appointmentEndTime;
    private int customerIDNumber;
    private int userIDNumber;
    private int contactIDNumber;

    /**
     * Appointment Constructor.
     *
     * @param appointmentIDNumber appointment ID.
     * @param appointmentTitle appointment title.
     * @param appointmentDescription appointment description.
     * @param appointmentLocation appointment location.
     * @param contactName contact name.
     * @param appointmentType appointment type.
     * @param appointmentStartDate appointment start date.
     * @param appointmentStartTime appointment start time.
     * @param appointmentEndDate appointment end date.
     * @param appointmentEndTime appointment end time.
     * @param customerIDNumber customer ID.
     * @param userIDNumber user ID.
     * @param contactIDNumber contact ID.
     */
    public Appointments(int appointmentIDNumber, String appointmentTitle, String appointmentDescription, String appointmentLocation, String contactName, String appointmentType, LocalDate appointmentStartDate,
                        LocalDateTime appointmentStartTime, LocalDate appointmentEndDate, LocalDateTime appointmentEndTime, int customerIDNumber, int userIDNumber, int contactIDNumber) {
        this.appointmentIDNumber = appointmentIDNumber;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.contactName = contactName;
        this.appointmentType = appointmentType;
        this.appointmentStartDate = appointmentStartDate;
        this.appointmentStartTime = appointmentStartTime;
        this.appointmentEndDate = appointmentEndDate;
        this.appointmentEndTime = appointmentEndTime;
        this.customerIDNumber = customerIDNumber;
        this.userIDNumber = userIDNumber;
        this.contactIDNumber = contactIDNumber;
    }

    /**
     * Appointment constructor for alerts.
     *
     * @param appointmentIDNumber is the unique ID associated with the Appointment.
     * @param appointmentStartDate appointment start date.
     * @param appointmentStartTime appointment start time.
     */
    public Appointments(int appointmentIDNumber, LocalDate appointmentStartDate, LocalDateTime appointmentStartTime) {
        this.appointmentIDNumber = appointmentIDNumber;
        this.appointmentStartDate = appointmentStartDate;
        this.appointmentStartTime = appointmentStartTime;
    }

    /**
     * Appointment constructor for the contact schedule report.
     *
     * @param appointmentIDNumber unique appointment id.
     * @param appointmentTitle appointment's name.
     * @param appointmentDescription appointment's description.
     * @param appointmentLocation appointment's location.
     * @param appointmentType appointment's type.
     * @param utcAppointmentStartTimestamp  the timestamp of the start of the appointment in UTC timezone.
     * @param utcAppointmentEndTimestamp  the timestamp of the start of the appointment in UTC timezone.
     * @param customerIDNumber unique customer id.
     * @param userIDNumber  unique user id.
     * @param contactIDNumber  unique contact id.
     * This constructor is used to display Appointments that are present in the database.
     * This constructor is used to contain retrieved Appointment data from the database and then show them in a table later.
     */
    public Appointments(int appointmentIDNumber, String appointmentTitle, String appointmentDescription, String appointmentLocation,
                        String appointmentType, String utcAppointmentStartTimestamp, String utcAppointmentEndTimestamp, int customerIDNumber, int userIDNumber, int contactIDNumber) {

        this.appointmentIDNumber = appointmentIDNumber;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;
        this.utcAppointmentStartTimestamp = utcAppointmentStartTimestamp;
        this.utcAppointmentEndTimestamp = utcAppointmentEndTimestamp;
        this.customerIDNumber = customerIDNumber;
        setCustomerNameAttendingAppointment(customerIDNumber);
        this.userIDNumber = userIDNumber;
        this.contactIDNumber = contactIDNumber;
        setContactNameAttendingAppointment(contactIDNumber);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        this.utcZonedDateTimeStart = ZonedDateTime.parse(utcAppointmentStartTimestamp + " " + ZoneId.of("UTC"), dateTimeFormatter);
        this.localZonedDateTimeStart = utcZonedDateTimeStart.withZoneSameInstant(ZoneId.systemDefault());
        setAppointmentStartTimestamps();

        this.utcZonedDateTimeEnd = ZonedDateTime.parse(utcAppointmentEndTimestamp + " " + ZoneId.of("UTC"), dateTimeFormatter);
        this.localZonedDateTimeEnd = utcZonedDateTimeEnd.withZoneSameInstant(ZoneId.systemDefault());
        setAppointmentEndTimestamps();

    }

    /**
     * This constructor is used for appointment construction used to show the count of appointments by month and type.
     * @param appointmentType -- shows the type of appointments.
     */
    public Appointments(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Appointments(int appointmentIDNumber, LocalTime apptStartTime, LocalTime apptEndTime,
                        String appointmentTitle, String appointmentType, String appointmentDescription, int customerIDNumber) {

        LocalTime.from(apptStartTime);
        LocalTime.from(apptEndTime);

        this.appointmentIDNumber = appointmentIDNumber;
        this.appointmentTitle = appointmentTitle;
        this.appointmentType = appointmentType;
        this.appointmentDescription = appointmentDescription;
        this.customerIDNumber = customerIDNumber;

    }

    public int getAppointmentIDNumber() {
        return appointmentIDNumber;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public String getContactName() {
        return contactName;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public LocalDate getAppointmentStartDate() {
        return appointmentStartDate;
    }

    public LocalDateTime getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public LocalDate getAppointmentEndDate() {
        return appointmentEndDate;
    }

    public LocalDateTime getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public int getCustomerIDNumber() {
        return customerIDNumber;
    }

    public int getUserIDNumber() {
        return userIDNumber;
    }

    public int getContactIDNumber() {
        return contactIDNumber;
    }

    /**
     * This method checks to ensure all text fields in the appointment form are filled out.
     * If there is/are empty field(s), error messages will be displayed prompting the user to fill out all fields.
     *
     * @return --> error messages if there's one or more empty field(s).
     */
    public static String appointmentTFValidation(String title, String description, String location, Object contact, String type, Object startDate,
                                                 Object startTime, Object endDate, Object endTime, Object customerID, Object userID, String empFieldErrMessage) {
        if (title.equals("")) {
            empFieldErrMessage += "The appointment title field cannot be empty. ";
        }
        if (description.equals("")) {
            empFieldErrMessage += "\nThe appointment description field cannot be empty. ";
        }
        if (location.equals("")) {
            empFieldErrMessage += "\nThe appointment location field cannot be empty. ";
        }
        if (contact == null) {
            empFieldErrMessage += "\nPlease select a contact. ";
        }
        if (type.equals("")) {
            empFieldErrMessage += "\nThe appointment type field cannot be empty. ";
        }
        if (startDate == null) {
            empFieldErrMessage += "\nPlease select a start date for the appointment. ";
        }
        if (startTime == null) {
            empFieldErrMessage += "\nPlease select a start time for the appointment. ";
        }
        if (endDate == null) {
            empFieldErrMessage += "\nPlease select an end date for the appointment. ";
        }
        if (endTime == null) {
            empFieldErrMessage += "\nPlease select an end time for the appointment. ";
        }
        if (customerID == null) {
            empFieldErrMessage += "\nPlease select a customer ID number. ";
        }
        if (userID == null) {
            empFieldErrMessage += "\nPlease select a user ID number. ";
        }
        return empFieldErrMessage;
    }

    /**
     * This method contains the error checks for adding new appointments.
     * The method will display a specific error(s) based on the appropriate scenarios.
     * @param alertChosen alert to be shown if there's an error.
     */
    public static void displayAppointmentErrorAlert(int alertChosen){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch(alertChosen) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid start/end date!");
                alert.setContentText("Appointments must start and end in the same day due to business operation constraints");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid start date!");
                alert.setContentText("An appointment's start date cannot be after an end date.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid start time!");
                alert.setContentText("An appointment's start time cannot be after an end time.'");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid start time!");
                alert.setContentText("An appointment's start and end times cannot be the same.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("Appointment overlap!");
                alert.setContentText("This appointment overlaps with an existing appointment");
                alert.showAndWait();
                break;
            case 6:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid start time!");
                alert.setContentText("An appointment's start time cannot be before the current local time.");
                alert.showAndWait();
                break;
            case 7:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid start date!");
                alert.setContentText("An appointment's start date cannot be before the current local date.'");
                alert.showAndWait();
                break;

            case 8:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid end date!");
                alert.setContentText("An appointment's end date cannot be before the current local date.'");
                alert.showAndWait();
                break;
            case 9:
                alert.setTitle("Error!");
                alert.setContentText("The appointment end time cannot be before the current local time.");
                alert.showAndWait();
                break;
            default:
                alert.setTitle("Error");
                alert.setHeaderText("One or more fields empty!");
                alert.setContentText("Please fill out all required fields");
                alert.showAndWait();
                break;
        }
    }

    /**
     * This method is used to set the name of the Customer that's attending the Appointment.
     * The Customer is identified by their unique customer ID number.
     *
     * @param customerIDNumber the unique ID connected with the Customer attending the Appointment.
     */
    public void setCustomerNameAttendingAppointment(int customerIDNumber) {
        try {
            String sqlQuery = "select Customer_Name from customers where Customer_ID = " + customerIDNumber;
            Statement statement = JDBC.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);

            while(rs.next()) {
                String customerName = rs.getString(1);
            }
        }
        catch(SQLException e) {
            System.out.println("There was an error retrieving Customer data from the database.");
        }
    }

    /**
     * This method sets the name of the Contact attending the Appointment.
     * The contact is identified by their unique Contact ID number.
     *
     * @param contactID unique ID number connected with the Contact attending the Appointment.
     */
    public void setContactNameAttendingAppointment(int contactID) {
        try {
            String query = "select Contact_Name from contacts where Contact_ID = " + contactID;
            Statement st = JDBC.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()) {
                this.contactName = rs.getString(1);
            }
        }
        catch(SQLException e) {
            System.out.println("There was an error retrieving Contact information from the database.");
        }
    }

    /**
     * This method sets the local and UTC timestamp of the start time of the Appointment in a format accepted by the database.
     */
    public void setAppointmentStartTimestamps() {
        DateTimeFormatter utcDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        this.utcAppointmentStartTimestamp = utcDateTimeFormatter.format(utcZonedDateTimeStart);
        String localAppointmentStartTimestamp = localDateTimeFormatter.format(localZonedDateTimeStart);
    }

    /**
     * This method sets the local and UTC timestamp of the end time of the Appointment in a format accepted by the database.
     */
    public void setAppointmentEndTimestamps() {
        DateTimeFormatter utcDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
        this.utcAppointmentEndTimestamp = utcDateTimeFormatter.format(utcZonedDateTimeEnd);
        String localAppointmentEndTimestamp = localDateTimeFormatter.format(localZonedDateTimeEnd);
    }
}

