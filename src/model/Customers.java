package model;

/**
 * This class creates is used to create customers.
 *
 * @author Sophie Dang.
 */
public class Customers {
    private int customerIDNumber;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhoneNumber;
    private String country;
    private String division;
    private int divisionIDNumber;
    private int countryIDNumber;


    /**
     * This constructor is used to create a new customer.
     * @param customerIDNumber customer ID.
     * @param customerName customer name.
     * @param customerAddress customer address.
     * @param customerPostalCode customer zip code.
     * @param customerPhoneNumber customer phone number.
     * @param country customer country.
     * @param division customer division.
     * @param divisionIDNumber division ID.
     * @param countryIDNumber country ID.
     */
    public Customers(int customerIDNumber, String customerName, String customerAddress, String customerPostalCode,
                     String customerPhoneNumber, String country, String division, int divisionIDNumber, int countryIDNumber) {
        this.customerIDNumber = customerIDNumber;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.country = country;
        this.division = division;
        this.divisionIDNumber = divisionIDNumber;
        this.countryIDNumber = countryIDNumber;
    }


    /**
     * Constructor used for Customers List.
     * @param customerIDNumber customer ID.
     * @param customerName customer name.
     */
    public Customers(int customerIDNumber, String customerName) {
        this.customerIDNumber = customerIDNumber;
        this.customerName = customerName;
    }


   //getters and setters.
    public int getCustomerIDNumber() {
        return customerIDNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public String getDivision() {
        return division;
    }

    public int getDivisionIDNumber() {
        return divisionIDNumber;
    }

    public int getCountryIDNumber() {
        return countryIDNumber;
    }

    public int retrieveCustomerIDNumber() {
        return customerIDNumber;
    }


    /**
     * This method checks to ensure all text fields in the customer form are filled out.
     * If there is/are empty field(s), error messages will be displayed prompting the user to fill out all fields.
     *
     * @return --> error messages if there's one or more empty field(s).
     */
    public static String textFieldsValidationCheck(String name, String address, String postalCode, String phoneNumber, Object country,
                                                   Object division, String empFieldErrMessage)
    {
        if (name.equals("")) {
            empFieldErrMessage += "The name field cannot be empty. ";
        }
        if (address.equals("")) {
            empFieldErrMessage += "\nThe address field cannot be empty. ";
        }
        if (postalCode.equals("")) {
            empFieldErrMessage += "\nThe postal code field cannot be empty. ";
        }
        if (phoneNumber.equals("")) {
            empFieldErrMessage += "\nThe phone number field cannot be empty. ";
        }
        if (country == null) {
            empFieldErrMessage += "\nPlease select a country. ";
        }
        if (division == null) {
            empFieldErrMessage += "\nPlease select a division. ";
        }
        return empFieldErrMessage;
    }

}
