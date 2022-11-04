package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;
import utilities.ChangeCurrentView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * This controller class provides logic for the Main Screen Form.
 *
 * @author Sophie Dang.
 */
public class MainScreenController {
    @FXML private TableView mainCustomersTableView;
    @FXML private TableColumn customerIDNumberCol;
    @FXML private TableColumn customerNameCol;
    @FXML private TableColumn customerAddressCol;
    @FXML private TableColumn customerPostalCodeCol;
    @FXML private TableColumn customerPhoneNumberCol;
    @FXML private TableColumn customerDivisionCol;
    @FXML private TableColumn customerCountryCol;
    @FXML private TableView mainAppointmentsTableView;
    @FXML private TableColumn appointmentIDNumberCol;
    @FXML private TableColumn appointmentTitleCol;
    @FXML private TableColumn appointmentTypeCol;
    @FXML private TableColumn appointmentDescriptionCol;
    @FXML private TableColumn appointmentLocationCol;
    @FXML private TableColumn appointmentStartDateTimeCol;
    @FXML private TableColumn appointmentEndDateTimeCol;
    @FXML private TableColumn appointmentContactCol;
    @FXML private TableColumn appointmentCustomerIDCol;
    @FXML private TableColumn appointmentUserIDCol;
    @FXML private RadioButton allAppointmentsRadioButton;
    @FXML private RadioButton weeklyAppointmentsRadioButton;
    @FXML private RadioButton monthlyAppointmentsRadioButton;
    @FXML private Label appointmentAlertLabel;
    private ObservableList chosenToggle;

    /**
     * This method initializes the appointments and customers' table views and populates the columns with the appropriate values.
     * The included lambda expression creates an alert if there's an upcoming appointment within 15 minutes of the user's current time.
     */
    public void initialize() throws SQLException {
        ObservableList<Appointments> listOfUpcomingAppointments = FXCollections.observableArrayList();
        ObservableList<Appointments> appointmentAlertList = AppointmentDAO.appointmentWithin15MinsAlerts();

        if (appointmentAlertList != null) {
            //This Lambda expression is used to create an alert if there are incoming appointments starting within the next 15 mins.
            appointmentAlertList.forEach(appointment -> {
                listOfUpcomingAppointments.add(appointment);
                String alertString = "There are upcoming appointments starting within the next 15 minutes:\n" + "Appointment ID: " + appointment.getAppointmentIDNumber() + "\n" +
                        "Date: " + appointment.getAppointmentStartDate() + "\n" + "Time: " + appointment.getAppointmentStartTime().toLocalTime();
                appointmentAlertLabel.setText(alertString);
            });
        }

        mainCustomersTableView.setItems(CustomerDAO.retrieveCustomerDataFromDatabase());
        customerIDNumberCol.setCellValueFactory(new PropertyValueFactory<>("customerIDNumber"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));

        mainAppointmentsTableView.setItems(AppointmentDAO.retrieveAppointmentDataFromDatabase());
        appointmentIDNumberCol.setCellValueFactory(new PropertyValueFactory<>("appointmentIDNumber"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStartDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentStartTime"));
        appointmentEndDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentEndTime"));
        appointmentCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerIDNumber"));
        appointmentUserIDCol.setCellValueFactory(new PropertyValueFactory<>("userIDNumber"));

    }


    /**
     * This method handles the all, weekly, and monthly appointments radio buttons.
     * The data in the appointments table view will change according to which button the user clicks on.
     *
     * @param actionEvent all, weekly, or monthly radio button clicked.
     */
    public void appointmentViewToggle(ActionEvent actionEvent) {
        if (allAppointmentsRadioButton.isSelected()) {
            chosenToggle = AppointmentDAO.retrieveAppointmentDataFromDatabase();
        } else if (weeklyAppointmentsRadioButton.isSelected()) {
            chosenToggle = AppointmentDAO.weeklyAppointmentsList();
        } else if (monthlyAppointmentsRadioButton.isSelected()) {
            chosenToggle = AppointmentDAO.monthlyAppointmentsList();
        }
        mainAppointmentsTableView.setItems(chosenToggle);
    }


    /**
     * This method handles the add customer button.
     * This method switches screens to the Customer FXML form for the user to add a customer.
     *
     * @param actionEvent --> add customer button clicked.
     * @throws IOException
     * @throws SQLException
     */
    public void mainViewsAddCustomerButtonClicked(ActionEvent actionEvent) throws IOException, SQLException {
        Customers customer = null;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/CustomerForm.fxml"));
        Parent scene = fxmlLoader.load();

        CustomerFormController customerController = fxmlLoader.getController();
        customerController.pullAppointmentDataFromDB(customer);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     * This method handles the update customer button.
     * This method retrieves the chosen customer from the Customer Table and fills the Customer FXML form to be edited.
     * If the user doesn't select any customer, then an error message will display prompting them to choose a customer to update.
     *
     * @param actionEvent --> update customer button clicked.
     * @throws IOException
     * @throws SQLException
     */
    public void mainViewsUpdateCustomerButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        Customers chosenCustomer = (Customers) mainCustomersTableView.getSelectionModel().getSelectedItem();

        if (chosenCustomer != null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/CustomerForm.fxml"));
            Parent scene = fxmlLoader.load();

            CustomerFormController customerController = fxmlLoader.getController();
            customerController.pullAppointmentDataFromDB(chosenCustomer);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(scene));
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Customer Selected!");
            alert.setContentText("Please select a customer to update.");
            alert.showAndWait();
        }
    }


    /**
     * This method handles the delete customer button.
     * This method deletes the selected customer from the database, which will remove them from the Customer Table and all associated Appointments
     * database and the Appointment Table View.
     * If no customer is selected by the user, then an error message will display telling them they need to select a customer.
     *
     * @param actionEvent-> delete customer button clicked.
     * @throws SQLException
     */
    public void mainViewsDeleteCustomerButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        Customers chosenCustomer = (Customers) mainCustomersTableView.getSelectionModel().getSelectedItem();

        if (chosenCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Please confirm you want to delete this customer and all associated appointments.");
            alert.setHeaderText("Delete " + chosenCustomer.getCustomerName() + " and any associated appointments.");
            Optional<ButtonType> response = alert.showAndWait();

            if (response.isPresent() && response.get() == ButtonType.OK) {
                CustomerDAO.deleteSelectedCustomer(chosenCustomer.retrieveCustomerIDNumber());
                AppointmentDAO.deleteAppointmentByCustomer(chosenCustomer.retrieveCustomerIDNumber());
                AppointmentDAO.deleteSelectedAppointment(chosenCustomer.retrieveCustomerIDNumber());
                mainCustomersTableView.setItems(CustomerDAO.createListOfCustomersFromDatabase());
                mainAppointmentsTableView.setItems(AppointmentDAO.retrieveAppointmentDataFromDatabase());
                mainCustomersTableView.setItems(CustomerDAO.retrieveCustomerDataFromDatabase());
            }
            new ChangeCurrentView(actionEvent, "MainScreenViews.fxml");

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Customer Selected!");
            alert.setContentText("Please select a customer to delete.");
            alert.showAndWait();
        }
    }


    /**
     * This method handles the add appointment button.
     * This method changes screens to the Appointment FXML form so the user can add an appointment.
     *
     * @param actionEvent -> add appointment button clicked.
     * @throws Exception
     */
    public void mainViewsAddAppointmentButtonClicked(ActionEvent actionEvent) throws IOException {
        Appointments appointments = null;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AppointmentForm.fxml"));
        Parent scene = loader.load();

        AppointmentFormController appointmentController = loader.getController();
        appointmentController.pullAppointmentsDataFromDatabase(appointments);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();

        new ChangeCurrentView(actionEvent, "AppointmentForm.fxml");
    }


    /**
     * This method handles the update appointment button.
     * This method changes screens to the Appointment FXML form and fills the fields with the selected appointment's data to be edited.
     * If no appointment was selected, then an error message will display telling the user they need to make a selection.
     *
     * @param actionEvent update appointment button click
     * @throws Exception
     */
    public void mainViewsUpdateAppointmentButtonClicked(ActionEvent actionEvent) throws IOException {
        Appointments chosenAppointment = (Appointments) mainAppointmentsTableView.getSelectionModel().getSelectedItem();

        if (chosenAppointment != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AppointmentForm.fxml"));
            Parent scene = loader.load();

            AppointmentFormController appointmentFormController = loader.getController();
            appointmentFormController.pullAppointmentsDataFromDatabase(chosenAppointment);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(scene));
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Appointment Selected!");
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();
        }
    }


    /**
     * This method handles delete appointment button.
     * This method deletes the appointment data from the database and the Appointment Table View.
     * If no appointment was selected, then an error message will display telling the user they have to select an appointment.
     *
     * @param actionEvent delete appointment button clicked.
     * @throws SQLException
     */
    public void mainViewsDeleteAppointmentButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        Appointments chosenAppointment = (Appointments) mainAppointmentsTableView.getSelectionModel().getSelectedItem();

        if (chosenAppointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Please confirm you want to delete the selected appointment.");
            alert.setHeaderText("Delete " + chosenAppointment.getAppointmentIDNumber() + chosenAppointment.getAppointmentTitle() + chosenAppointment.getAppointmentType());
            Optional<ButtonType> response = alert.showAndWait();

            if (response.isPresent() && response.get() == ButtonType.OK) {
                AppointmentDAO.deleteSelectedAppointment(chosenAppointment.getAppointmentIDNumber());
                mainAppointmentsTableView.setItems(AppointmentDAO.retrieveAppointmentDataFromDatabase());
            }
            new ChangeCurrentView(actionEvent, "MainScreenViews.fxml");

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Appointment Selected!");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
        }
    }


    /**
     * This method handles the reports button.
     * This method switches screens to the Reports main screen.
     *
     * @param actionEvent reports button clicked.
     * @throws IOException
     */
    public void mainViewsReportsButtonClicked(ActionEvent actionEvent) throws IOException {
        new ChangeCurrentView(actionEvent, "MainReportsScreen.fxml");
    }

    /**
     * The exit button lets the user exit the main screen and is redirected to the navigation screen.
     *
     * @param actionEvent exit button clicked.
     * @throws IOException
     */
    public void mainViewsExitButtonClicked(ActionEvent actionEvent) throws IOException {
        new ChangeCurrentView(actionEvent, "NavigationScreen.fxml");
    }
}
