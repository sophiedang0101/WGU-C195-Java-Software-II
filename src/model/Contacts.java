package model;

/**
 * This class creates new contacts.
 *
 * @author Sophie Dang.
 */
public class Contacts {

    private int contactIDNumber;
    private String contactName;
    private String contactEmail;
    public Contacts(int contactIDNumber, String contactName, String contactEmail) {
        this.contactIDNumber = contactIDNumber;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public int getContactIDNumber() {
        return contactIDNumber;
    }

    public String getContactName() {
        return contactName;
    }
}
