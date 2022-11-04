package model;

/**
 * This class creates a country.
 *
 * @author Sophie Dang.
 */
public class Country {
    private final String countryName;

    public Country(int countryIDNumber, String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }
}
