package model;

/**
 * @author Sophie Dang.
 */
public class Report {
    private int divisionTotal;
    private String divisionName;

    /**
     * This constructor is used for each customer by division report.
     *
     * @param divisionTotal total divisions.
     * @param divisionName name of the division.
     */
    public Report(int divisionTotal, String divisionName) {
        this.divisionTotal = divisionTotal;
        this.divisionName = divisionName;
    }

    public int getDivisionTotal() {
        return divisionTotal;
    }

    public void setDivisionTotal(int divisionTotal) {
        this.divisionTotal = divisionTotal;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
}
