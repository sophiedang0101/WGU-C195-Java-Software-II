package model;

/**
 * This class creates a user.
 *
 * @author Sophie Dang
 */
public class User {
    private final int userIDNumber;
    private final String userName;

    public User(int userIDNumber, String userName) {
        this.userIDNumber = userIDNumber;
        this.userName = userName;
    }

    public int getUserIDNumber() {
        return userIDNumber;
    }

    public String getUserName() {
        return userName;
    }
}
