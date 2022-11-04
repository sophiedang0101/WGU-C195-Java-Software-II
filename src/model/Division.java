package model;

/**
 * This class creates a division.
 *
 * @author Sophie Dang.
 */
public class Division {
    private int divisionIDNumber;
    private String divisionName;

    public Division(int divisionIDNumber, int countryIDNumber, String divisionName) {
        this.divisionIDNumber = divisionIDNumber;
        this.divisionName = divisionName;
    }

    public int getDivisionIDNumber() {
        return divisionIDNumber;
    }

    public String getDivisionName() {
        return divisionName;
    }
}
