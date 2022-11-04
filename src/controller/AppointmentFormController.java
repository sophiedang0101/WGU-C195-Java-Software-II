package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Appointments;
import model.Contacts;
import model.Customers;
import model.User;
import utilities.ChangeCurrentView;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * This controller class provides logic for the Appointment Form.
 *
 * @author Sophie Dang.
 */
public class AppointmentFormController {
    @FXML private TextField appointmentTitleTF;
    @FXML private TextField appointmentDescriptionTF;
    @FXML private TextField appointmentLocationTF;
    @FXML private ComboBox appointmentContactComboBox;
    @FXML private ComboBox appointmentUserIDComboBox;
    @FXML private ComboBox appointmentStartTimeComboBox;
    @FXML private ComboBox appointmentEndTimeComboBox;
    @FXML private ComboBox appointmentCustomerIDComboBox;
    @FXML private DatePicker appointmentStartDatePicker;
    @FXML private DatePicker appointmentEndDatePicker;
    @FXML private TextField appointmentIDNumberTF;
    @FXML private TextField appointmentTypeTF;
    @FXML String empFieldsErrMessage = "";

    /**
     * This method pulls appointment data from the database and inserts it into the appropriate fields in the appointment form.
     *
     * @param appointments appointments.
     */
    public void pullAppointmentsDataFromDatabase(Appointments appointments) {
        ObservableList<String> listOfContacts = FXCollections.observableArrayList();
        ObservableList<String> listOfCustomers = FXCollections.observableArrayList();
        ObservableList<String> listOfUsers = FXCollections.observableArrayList();
        ObservableList<String> listOfTimes = FXCollections.observableArrayList();

        try {
            ObservableList<Contacts> contactsObservableList = ContactDAO.retrieveContactDataFromDatabase();
            if (contactsObservableList != null) {
                for (Contacts contact : contactsObservableList) {
                    listOfContacts.add(contact.getContactIDNumber() + " - " + contact.getContactName());
                }
            }

            ObservableList<Customers> customersObservableList = CustomerDAO.retrieveCustomerDataFromDatabase();
            if (customersObservableList != null) {
                for (Customers customer : customersObservableList) {
                    listOfCustomers.add(customer.getCustomerIDNumber() + " - " + customer.getCustomerName());
                }
            }

            ObservableList<User> usersObservableList = UserDAO.createListOfUsersFromDatabase();
            if (usersObservableList != null) {
                for (User user : usersObservableList) {
                    listOfUsers.add(user.getUserIDNumber() + " - " + user.getUserName());
                }
            }

            LocalTime startHour = LocalTime.of(8, 0);
            LocalTime endHour = LocalTime.of(22, 0);
            listOfTimes.add(startHour.toString());

            while (startHour.isBefore(endHour)) {
                startHour = startHour.plusMinutes(5);
                listOfTimes.add(startHour.toString());
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }

        appointmentContactComboBox.setItems(listOfContacts);
        appointmentCustomerIDComboBox.setItems(listOfCustomers);
        appointmentUserIDComboBox.setItems(listOfUsers);
        appointmentStartTimeComboBox.setItems(listOfTimes);
        appointmentEndTimeComboBox.setItems(listOfTimes);

        if (appointments != null) try {
            appointmentIDNumberTF.setText(Integer.toString(appointments.getAppointmentIDNumber()));
            appointmentTitleTF.setText(appointments.getAppointmentTitle());
            appointmentDescriptionTF.setText(appointments.getAppointmentDescription());
            appointmentLocationTF.setText(appointments.getAppointmentLocation());
            appointmentContactComboBox.setValue(appointments.getContactIDNumber() + " - " + appointments.getContactName());
            appointmentTypeTF.setText(appointments.getAppointmentType());
            appointmentStartDatePicker.setValue(appointments.getAppointmentStartDate());
            appointmentStartTimeComboBox.setValue(appointments.getAppointmentStartTime().toLocalTime());
            appointmentEndDatePicker.setValue(appointments.getAppointmentEndDate());
            appointmentEndTimeComboBox.setValue(appointments.getAppointmentEndTime().toLocalTime());
            appointmentCustomerIDComboBox.setValue(appointments.getCustomerIDNumber());
            appointmentUserIDComboBox.setValue(appointments.getUserIDNumber());

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
    }


    /**
     * This method removes non-integer values from the Contact Combo Box.
     *
     * @return
     */
    public int removeNonIntValuesFromContactCB() {
        String contactIDCBStr = String.valueOf(appointmentContactComboBox.getValue());
        String contactCBInteger = contactIDCBStr.replaceAll("\\D+", "");
        return Integer.parseInt(contactCBInteger);
    }

    /**
     * This method removes non-integer values from the Customer Combo Box.
     *
     * @return
     */
    public int removeNonIntValuesFromCustomerCB() {
        String customerIDCBStr = String.valueOf(appointmentCustomerIDComboBox.getValue());
        String customerCBInteger = customerIDCBStr.replaceAll("\\D+", "");
        return Integer.parseInt(customerCBInteger);
    }


    /**
     * This method removes non-integer values from the User Combo Box.
     *
     * @return
     */
    public int removeNonIntValuesFromUserCB() {
        String userIDCBStr = String.valueOf(appointmentUserIDComboBox.getValue());
        String userCBInteger = userIDCBStr.replaceAll("\\D+", "");
        return Integer.parseInt(userCBInteger);
    }


    /**
     * This method handles the save button on the Appointments Screen.
     * The method checks whether there are any empty text fields,invalid inputs, appointment times overlap,and will display an error alert if there are.
     * If all text fields are filled, then the method will save the appointment as a new appointment if the appointment ID field is empty.
     * If the appointment ID field isn't empty, the method will update the existing appointment in the database.
     * After the method saves the data to the database, it will take the user back to the main table views screen.
     *
     * @param actionEvent --> save button clicked.
     * @throws Exception
     */
    public void appointmentSaveButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        String title = appointmentTitleTF.getText();
        String description = appointmentDescriptionTF.getText();
        String location = appointmentLocationTF.getText();
        String type = appointmentTypeTF.getText();
        Object contact = appointmentContactComboBox.getValue();
        Object startDate = appointmentStartDatePicker.getValue();
        Object startTime = appointmentStartTimeComboBox.getValue();
        Object endDate = appointmentEndDatePicker.getValue();
        Object endTime = appointmentEndTimeComboBox.getValue();
        Object customerID = appointmentCustomerIDComboBox.getValue();
        Object userID = appointmentUserIDComboBox.getValue();

        empFieldsErrMessage = Appointments.appointmentTFValidation(title,description,location,contact,type,startDate,startTime,endDate,endTime,
                                                                customerID,userID,empFieldsErrMessage);

        if (empFieldsErrMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields Warning");
            alert.setContentText(empFieldsErrMessage);
            alert.showAndWait();
            empFieldsErrMessage = "";
            return;

        }
        else if(appointmentDateAndTimeValidator()){
            return;

        }
        else if (appointmentIDNumberTF.getText().isEmpty()) {
            AppointmentDAO.createNewAppointment(
                    appointmentTitleTF.getText(),
                    appointmentDescriptionTF.getText(),
                    appointmentLocationTF.getText(),
                    removeNonIntValuesFromContactCB(),
                    appointmentTypeTF.getText(),
                    LocalDateTime.of(appointmentStartDatePicker.getValue(),
                            LocalTime.parse(appointmentStartTimeComboBox.getSelectionModel().getSelectedItem().toString())),
                    LocalDateTime.of(appointmentEndDatePicker.getValue(),
                            LocalTime.parse(appointmentEndTimeComboBox.getSelectionModel().getSelectedItem().toString())),
                    removeNonIntValuesFromCustomerCB(),
                    (removeNonIntValuesFromUserCB()));

        } else try {
            AppointmentDAO.updateSelectedAppointment(
                    Integer.parseInt(appointmentIDNumberTF.getText()),
                    appointmentTitleTF.getText(),
                    appointmentDescriptionTF.getText(),
                    appointmentLocationTF.getText(),
                    removeNonIntValuesFromContactCB(),
                    appointmentTypeTF.getText(),
                    LocalDateTime.of(appointmentStartDatePicker.getValue(),
                            LocalTime.parse(appointmentStartTimeComboBox.getSelectionModel().getSelectedItem().toString())),
                    LocalDateTime.of(appointmentEndDatePicker.getValue(),
                            LocalTime.parse(appointmentEndTimeComboBox.getSelectionModel().getSelectedItem().toString())),
                    removeNonIntValuesFromCustomerCB(),
                    (removeNonIntValuesFromUserCB()));

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }

        new ChangeCurrentView(actionEvent, "MainScreenViews.fxml");
    }


    /**
     * This method handles the back button on the Appointments Form.
     * The method redirects the user back to the main appointments and customers table views screen.
     *
     * @param actionEvent --> back button clicked.
     */
    public void backToMainScreenButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text fields and redirect you to the main menu. " +
                "All information entered will not be saved. Please confirm you want to continue.");
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            new ChangeCurrentView(actionEvent,"MainScreenViews.fxml");
        }
    }

    /**
     * This method checks the information in the Start Time CB, End Time CB, Start Date Picker, and End Date Picker to ensure the inputs are valid.
     * The method ensures that times/dates don't start after the end or start before the current local date/time.
     * The method contains error checking for business days, invalid entries and appointment overlaps.
     * The method uses error messages from the displayAppointmentErrorAlert() method from the Appointments class.
     * @return returns true and display an error alert and false is the times/dates are valid.
     */
    public boolean appointmentDateAndTimeValidator() throws SQLException {
        LocalDateTime appointmentStartTime = LocalDateTime.of(appointmentStartDatePicker.getValue(),
                LocalTime.parse(appointmentStartTimeComboBox.getSelectionModel().getSelectedItem().toString()));
        LocalDateTime appointmentEndTime = LocalDateTime.of(appointmentEndDatePicker.getValue(),
                LocalTime.parse(appointmentEndTimeComboBox.getSelectionModel().getSelectedItem().toString()));

        LocalTime apptStartTime = LocalTime.from(appointmentStartTime);
        LocalTime apptEndTime = LocalTime.from(appointmentEndTime);

       if(appointmentStartTime.equals(appointmentEndTime)){
           Appointments.displayAppointmentErrorAlert(4);
           return true;
       }
       if(appointmentStartDatePicker.getValue().equals(LocalDate.now()) && apptStartTime.isBefore(LocalTime.now()) ){
           Appointments.displayAppointmentErrorAlert(6);
           return true;
       }
       if(appointmentStartDatePicker.getValue().equals(LocalDate.now()) && apptEndTime.isBefore(LocalTime.now())){
           Appointments.displayAppointmentErrorAlert(9);
           return true;
       }
       if(appointmentStartDatePicker.getValue().isBefore(LocalDate.now())){
           Appointments.displayAppointmentErrorAlert(7);
           return true;
       }
       if(appointmentEndDatePicker.getValue().isBefore(LocalDate.now())){
           Appointments.displayAppointmentErrorAlert(8);
           return true;
       }
       if(appointmentStartTime.isAfter(appointmentEndTime)){
           Appointments.displayAppointmentErrorAlert(3);
           return true;
       }

       if(appointmentStartDatePicker.getValue().isBefore(appointmentEndDatePicker.getValue()) ||
               appointmentStartDatePicker.getValue().isAfter(appointmentEndDatePicker.getValue())){
           Appointments.displayAppointmentErrorAlert(1);
           return true;
       }
           if(appointmentOverlap()){
               Appointments.displayAppointmentErrorAlert(5);
               return true;
           }
        return false;
       }


    /**
     * This method validates whether the new appointment's times that the user wants to add overlap with an existing appointment.
     * Based on conflicting start/end times for other existing appointments.
     * @return true if the new appointment's times overlap with an existing appointment and false if not.
     * @throws SQLException
     */
    public Boolean appointmentOverlap() throws SQLException {
        LocalDateTime appointmentStartTime = LocalDateTime.of(appointmentStartDatePicker.getValue(),
                LocalTime.parse(appointmentStartTimeComboBox.getSelectionModel().getSelectedItem().toString()));
        LocalDateTime appointmentEndTime = LocalDateTime.of(appointmentEndDatePicker.getValue(),
                LocalTime.parse(appointmentEndTimeComboBox.getSelectionModel().getSelectedItem().toString()));

        ObservableList<Appointments> appointmentsList = AppointmentDAO.retrieveAppointmentDataFromDatabase();
        LocalTime appointmentStartLocalTime = LocalTime.from(appointmentStartTime);
        LocalTime appointmentEndLocalTime = LocalTime.from(appointmentEndTime);

        LocalDateTime appointmentStartLocalDateTime = LocalDateTime.of(appointmentStartDatePicker.getValue(), appointmentStartLocalTime);
        LocalDateTime appointmentEndDateTime = LocalDateTime.of(appointmentEndDatePicker.getValue(), appointmentEndLocalTime);

        assert appointmentsList != null;
        for(Appointments appointment: appointmentsList){
            if((appointmentStartLocalDateTime.isBefore(appointment.getAppointmentStartTime()) && appointmentEndDateTime.isAfter(appointment.getAppointmentEndTime()))
                    || (appointmentStartLocalDateTime.isAfter(appointment.getAppointmentStartTime()) && appointmentEndDateTime.isBefore(appointment.getAppointmentEndTime()))
                    || ((appointmentStartLocalDateTime.isAfter(appointment.getAppointmentStartTime()) && appointmentStartLocalDateTime.isBefore(appointment.getAppointmentEndTime()))
                    && appointmentEndDateTime.isAfter(appointment.getAppointmentEndTime())) || ((appointmentEndDateTime.isBefore(appointment.getAppointmentEndTime())
                    && appointmentEndDateTime.isAfter(appointment.getAppointmentStartTime())) && appointmentStartLocalDateTime.isBefore(appointment.getAppointmentStartTime()))
                    || (appointmentStartLocalDateTime.equals(appointment.getAppointmentStartTime()) && appointmentEndDateTime.equals(appointment.getAppointmentEndTime()))
                    || ((appointmentStartLocalDateTime.isAfter(appointment.getAppointmentStartTime()) && appointmentStartLocalDateTime.isBefore(appointment.getAppointmentEndTime()))
                    && appointmentEndDateTime.isEqual(appointment.getAppointmentEndTime())) || ((appointmentEndDateTime.isBefore(appointment.getAppointmentEndTime()) && appointmentEndDateTime.isAfter(appointment.getAppointmentStartTime()))
                    && appointmentStartLocalDateTime.isEqual(appointment.getAppointmentStartTime())) || (appointmentStartLocalDateTime.isBefore(appointment.getAppointmentStartTime())
                    && appointmentEndDateTime.isEqual(appointment.getAppointmentEndTime())) || (appointmentStartLocalDateTime.isEqual(appointment.getAppointmentStartTime())
                    && appointmentEndDateTime.isAfter(appointment.getAppointmentEndTime()))){
                return true;
            }
        }
        return false;
    }
}






