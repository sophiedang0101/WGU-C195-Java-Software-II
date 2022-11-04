package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import database.JDBC;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Appointments;
import model.Contacts;
import model.Report;
import utilities.ChangeCurrentView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
/**
 * This reports controller class handles different types of reports for the application.
 * Reports include total appointments by type and month, contacts schedule, users schedule, user login activity, and total number of customers by div.
 *
 * @author Sophie Dang.
 */
public class MainReportsScreenController implements Initializable {
    @FXML private Button backButton;
    @FXML private TableView reportsTableView;
    @FXML private RadioButton totalAppointmentsRadButton;
    @FXML private RadioButton usersScheduleRadButton;
    @FXML private ToggleGroup reportToggleGroup;
    @FXML private TableView contactScheduleTable;
    @FXML private TableColumn appointmentId;
    @FXML private TableColumn appointmentTitle;
    @FXML private TableColumn appointmentType;
    @FXML private TableColumn appointmentDescription;
    @FXML private TableColumn appointmentLocation;
    @FXML private TableColumn appointmentStart;
    @FXML private TableColumn appointmentEnd;
    @FXML private TableColumn appointmentCustomerId;
    @FXML private TableView<Report> customerByDivisionTableView;
    @FXML private TableColumn<Report, String> divisionName;
    @FXML private TableColumn<Report, String> divisionTotal;
    @FXML private Tab loginActivityTab;
    @FXML private TextArea userLoginActivityTA;
    private static final String userLoginActivityFile = "login_activity.txt";
    @FXML private ComboBox contactsComboBox;
    String sqlQuery;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sortByRadButtonClicked();
        fillUserLoginActivityTextArea();
        try {
            customerByDivisionTableView.setItems(createTotalCustomersForEachDivReport());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Contacts> contactsList = ContactDAO.retrieveContactDataFromDatabase();
        ObservableList<String> listOfContactNames = FXCollections.observableArrayList();
        assert contactsList != null;
        for (Contacts contacts : contactsList) {
            listOfContactNames.add(contacts.getContactName());
        }
        contactsComboBox.setItems(listOfContactNames);
        appointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentIDNumber"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("appointmentStartTime"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("appointmentEndTime"));
        appointmentCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerIDNumber"));
        divisionName.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        divisionTotal.setCellValueFactory(new PropertyValueFactory<>("divisionTotal"));

    }

    /**
     * This method loads the table view to the correct data based on which radio button the user selects.
     */
    public void sortByRadButtonClicked() {
        if (totalAppointmentsRadButton.isSelected()) {
            sqlQuery = ("select Type, MONTHNAME(Start) as Month, count(*) as Count from " +
                    "appointments group by Type, MONTHNAME(Start) order by Type;");
        } else if (usersScheduleRadButton.isSelected()) {
            sqlQuery = ("select users.User_Name, contacts.Contact_Name, appointments.Appointment_ID, " +
                    "appointments.Title, appointments.Type, appointments.Description, CONVERT_TZ(start, '+00:00', 'system') as start, " +
                    "CONVERT_TZ(end, '+00:00', 'system') as end, appointments.Customer_ID from appointments, contacts, users where appointments.Contact_ID = contacts.Contact_ID order by User_Name;");
        }
        generateListOfReports();
    }

    /**
     * This method creates a list for the total appointments and user schedules reports table by retrieving table data.
     * The lambda expression inside the method is used to dynamically generate the table list.
     */
    public void generateListOfReports() {
        Connection conn;
        ObservableList<Object> reportsDataList = FXCollections.observableArrayList();
        reportsTableView.getColumns().clear();

        try {
            conn = JDBC.getConnection();
            String sqlString = sqlQuery;
            ResultSet resultSet = conn.createStatement().executeQuery(sqlString);

            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int k = i;

                TableColumn tableColumn = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
                //This lambda expression is used to dynamically generate the table list for the reports table view.
                //The reports table view contains the total appointments by type and month and users schedule.
                tableColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                        new SimpleStringProperty(param.getValue().get(k).toString()));

                reportsTableView.getColumns().addAll(tableColumn);
            }
            while (resultSet.next()) {
                ObservableList<String> listOfRows = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    listOfRows.add(resultSet.getString(i));
                }
                reportsDataList.add(listOfRows);
            }
            reportsTableView.setItems(reportsDataList);

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
    }


    /**
     * This method creates data for the report of total numbers of customers for each division.
     * This task is achieved by using an SQL statement that groups together division IDs and performs a count for each division.
     * The method shows each division name and the total for the division in the appropriate table.
     *
     * @return list of reports.
     * @throws SQLException
     */
    private static ObservableList<Report> createTotalCustomersForEachDivReport() throws SQLException {
        ObservableList<Report> listOfCustomReports = FXCollections.observableArrayList();
        Connection connection = JDBC.getConnection();
        String sqlQueryString = "SELECT first_level_divisions.Division, COUNT(*) AS divisionCount FROM customers INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID WHERE first_level_divisions.Division_ID = customers.Division_ID GROUP BY first_level_divisions.Division_ID ORDER BY COUNT(*) DESC";
        JDBC.setPreparedStatement(connection, sqlQueryString);
        PreparedStatement ps = JDBC.getPreparedStatement();
        assert ps != null;
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String divisionName = rs.getString("Division");
            int divisionTotal = rs.getInt("divisionCount");
            Report report = new Report(divisionTotal, divisionName);
            listOfCustomReports.add(report);
        }
        return listOfCustomReports;
    }


    /**
     * This method handles the select contact combo box for the contacts schedule table.
     * The method loads all the appointments connected with the selected contact.
     *
     * @param actionEvent contact combo box clicked.
     * @throws SQLException
     */
    public void contactScheduleDropDown(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointments> appointmentsList = AppointmentDAO.retrieveAppointmentDataFromDatabase();
        ObservableList<Appointments> filteredListOfAppointments = FXCollections.observableArrayList();
        String chosenContact = (String) contactsComboBox.getSelectionModel().getSelectedItem();
        assert appointmentsList != null;
        for (Appointments appointments : appointmentsList) {
            if (chosenContact.equals(appointments.getContactName())) {
                filteredListOfAppointments.add(appointments);
            }
        }
        contactScheduleTable.setItems(filteredListOfAppointments);
    }


    /**
     * This method adds and records the user login activity from the readUserLoginActivityData method.
     */
    public void fillUserLoginActivityTextArea() {
        userLoginActivityTA.setText(String.valueOf(readUserLoginActivityData()));
    }


    /**
     * This method reads the data in the user login activity txt file: login_activity.txt.
     *
     * @return the lines (data) from the user login activity txt file.
     */
    public List<String> readUserLoginActivityData() {
        List<String> linesDataList = new ArrayList<>();
        String dataString;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(userLoginActivityFile));
            while ((dataString = bufferedReader.readLine()) != null) {
                linesDataList.add(dataString + "\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linesDataList;
    }

    /**
     * This method changes the current view and redirects the user back to the Main Screen Views Screen.
     *
     * @param actionEvent back to main screen button clicked.
     * @throws IOException
     */
    public void backToMainButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to go back to the main screen?");
        alert.showAndWait();
        new ChangeCurrentView(actionEvent, "MainScreenViews.fxml");
    }
}
