package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.DivisionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Country;
import model.Customers;
import model.Division;
import utilities.ChangeCurrentView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * This controller class provides logic for the Customer Form.
 *
 * @author Sophie Dang.
 */
public class CustomerFormController {
    @FXML private TextField customerIDNumberTF;
    @FXML private TextField customerNameTF;
    @FXML private TextField customerAddressTF;
    @FXML private TextField customerPostalCodeTF;
    @FXML private TextField customerPhoneNumberTF;
    @FXML private ComboBox customerCountryComboBox;
    @FXML private ComboBox customerDivisionComboBox;
    @FXML private Button customerFormSaveButton;
    @FXML private Button customerFormCancelButton;
    @FXML private Label customerCountryLabel;
    @FXML private Label customerDivisionLabel;
    @FXML private ObservableList listOfCountries = FXCollections.observableArrayList();
    @FXML String empFieldsErrMessage = "";


    /**
     * This method removes non-integer values from the division combo box.
     *
     * @return
     */
    public String removeNonIntValuesFromDivisionCB() {
        String divCBString = (String) customerDivisionComboBox.getValue();
        return divCBString.replaceAll("\\D+", "");
    }


    /**
     * This method retrieves appointment data from the database and inserts it into the appropriate text fields.
     *
     * @param customer customer.
     * @throws SQLException
     */
    public void pullAppointmentDataFromDB(Customers customer) throws SQLException {
        try {
            ObservableList<Country> countriesList = CountryDAO.retrieveCountryDataFromDatabase();
            if (countriesList != null) {
                for (Country country : countriesList) {
                    listOfCountries.add(country.getCountryName());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        customerCountryComboBox.setItems(listOfCountries);
        if (customer != null) try {
            customerIDNumberTF.setText(Integer.toString(customer.getCustomerIDNumber()));
            customerAddressTF.setText((customer.getCustomerAddress()));
            customerNameTF.setText(customer.getCustomerName());
            customerPostalCodeTF.setText((customer.getCustomerPostalCode()));
            customerPhoneNumberTF.setText((customer.getCustomerPhoneNumber()));
            customerCountryComboBox.setValue(customer.getCountryIDNumber() + " - " + customer.getCountry());
            customerDivisionComboBox.setValue(customer.getDivisionIDNumber() + " - " + customer.getDivision());

        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            exception.printStackTrace();
        }
    }


    /**
     * This method handles the save button on the customer form screen.
     * The method checks if there are any empty fields and will display an error message.
     * If all fields are completed, then the method will save the customer as a new customer if the customerID field is empty.
     * If the customerID field isn't empty, the method will update the existing customer in the database.
     * After the method saves the data to the database, it will take the user back to the main table views screen.
     *
     * @param actionEvent --> save button clicked.
     * @throws Exception
     */
    public void customerFormSaveButtonClicked(ActionEvent actionEvent) throws SQLException, IOException {
        String name = customerNameTF.getText();
        String address = customerAddressTF.getText();
        String postalCode = customerPostalCodeTF.getText();
        String phoneNumber = customerPhoneNumberTF.getText();
        Object country = customerCountryComboBox.getValue();
        Object division = customerDivisionComboBox.getValue();

        empFieldsErrMessage = Customers.textFieldsValidationCheck(name, address, postalCode, phoneNumber, country, division, empFieldsErrMessage);
        if (empFieldsErrMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Fields Warning");
            alert.setContentText(empFieldsErrMessage);
            alert.showAndWait();
            empFieldsErrMessage = "";
            return;

        } else if (customerIDNumberTF.getText().isEmpty()) {
            CustomerDAO.createNewCustomer(
                    customerNameTF.getText(),
                    customerAddressTF.getText(),
                    customerPostalCodeTF.getText(),
                    customerPhoneNumberTF.getText(),
                    removeNonIntValuesFromDivisionCB());
        } else try {
            CustomerDAO.updateSelectedCustomer(
                    Integer.parseInt(customerIDNumberTF.getText()),
                    customerNameTF.getText(),
                    customerAddressTF.getText(),
                    customerPostalCodeTF.getText(),
                    customerPhoneNumberTF.getText(),
                    removeNonIntValuesFromDivisionCB()
            );
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        new ChangeCurrentView(actionEvent, "MainScreenViews.fxml");
    }


    /**
     * This method handles the back button, and it'll check if the user wants to cancel adding or updating a customer.
     * If the user answers no, then it'll let the user stay on the page.
     * If the user answers yes, then the appointment screen will not send any data to the database and returns the user to the main table views screen.
     *
     * @param actionEvent --> cancel button clicked.
     * @throws Exception
     */
    public void customerFormBackButtonClicked(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text fields and redirect you to the main menu. " +
                "All information entered will not be saved. Please confirm you want to continue.");
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            new ChangeCurrentView(actionEvent, "MainScreenViews.fxml");
        }
    }


    /**
     * This method retrieves data to create a list of countries to fill the country combo box.
     *
     * @param actionEvent selects a country from the list.
     * @throws SQLException
     */
    public void retrieveCountryData(ActionEvent actionEvent) {
        ObservableList<String> listOfDivs = FXCollections.observableArrayList();
        try {
            ObservableList<Division> divisionsList = new DivisionDAO().retrieveDivisionDataFromDatabase((String) customerCountryComboBox.getSelectionModel().getSelectedItem());
            if (divisionsList != null) {
                for (Division division : divisionsList) {
                    listOfDivs.add(division.getDivisionIDNumber() + " - " + division.getDivisionName());
                }
            }
            customerDivisionComboBox.setItems(listOfDivs);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
