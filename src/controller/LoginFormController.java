package controller;

import database.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utilities.ChangeCurrentView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This controller class provides logic for the Login Form.
 *
 * @author Sophie Dang.
 */
public class LoginFormController implements Initializable {
    @FXML private TextField loginFormPasswordTF;
    @FXML private TextField loginFormUsernameTF;
    @FXML private Label loginPasswordLabel;
    @FXML private Label loginUsernameLabel;
    @FXML private Button loginButton;
    @FXML private Button loginFormResetButton;
    @FXML private Label loginFormTitleLabel;
    private final ResourceBundle localLanguage = ResourceBundle.getBundle("resource/language");
    @FXML private Label languageLabel;
    @FXML private Label locationLabel;
    @FXML private Button loginExitButton;
    private int loggedUserIDNumber;

    /**
     * This method opens a connection to the database and sets the location/language based on the end-user's machine.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.startConnection();
        setLocationAndLanguage();

    }

    /**
     * This method sets the location and language of the application based on the end-user's machine.
     * It sets the text labels on the login form to the appropriate language of either English or French.
     */
    private void setLocationAndLanguage() {
        ZoneId zoneId = ZoneId.systemDefault();
        Locale locale = Locale.getDefault();
        String userLocation = zoneId.systemDefault().toString();
        String userLanguage = locale.getDefault().getDisplayLanguage();

        ResourceBundle languageBundle = ResourceBundle.getBundle("resource/language", locale);

        loginFormResetButton.setText(localLanguage.getString("Reset"));
        loginButton.setText(localLanguage.getString("Login"));
        loginFormTitleLabel.setText(languageBundle.getString("Title"));
        loginUsernameLabel.setText(languageBundle.getString("Username"));
        loginPasswordLabel.setText(languageBundle.getString("Password"));
        locationLabel.setText(languageBundle.getString("Location") + userLocation);
        languageLabel.setText(languageBundle.getString("Language") + userLanguage);
        loginExitButton.setText(languageBundle.getString("Exit"));
    }


    /**
     * This method checks whether the password field is empty or not.
     * If the field is empty, it displays an error message prompting the user to enter a value for the password text field.
     *
     * @param password login password.
     */
    private void validatePasswordFieldNotEmpty(String password) {
        if (password.isEmpty()) {
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(localLanguage.getString("errorDialog"));
                alert.setContentText(localLanguage.getString("passwordRequired"));
                alert.showAndWait();
            }
        }
    }


    /**
     * This method checks whether the username field is empty.
     * If it's empty, the method displays an error message prompting the user to enter a value for the username text field.
     *
     * @param username username.
     */
    private void validateUsernameFieldNotEmpty(String username) {
        if (username.isEmpty()) {
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(localLanguage.getString("errorDialog"));
                alert.setContentText(localLanguage.getString("usernameRequired"));
                alert.showAndWait();
            }
        }
    }


    /**
     * This method handles the reset button. It clears out the username and password text fields so the user can re-enter the credentials.
     *
     * @param actionEvent --> reset button is clicked.
     */
    public void loginFormResetButtonClicked(ActionEvent actionEvent) {
        loginFormUsernameTF.clear();
        loginFormPasswordTF.clear();
        loginFormUsernameTF.setPromptText(localLanguage.getString("Username"));
        loginFormPasswordTF.setPromptText(localLanguage.getString("Password"));
    }


    /**
     * This method handles the login button.
     * This method checks whether the username and password entered match the ones in the database.
     * If the username or password doesn't belong to a user in the database or a field is empty, then the method will display an error message.
     *
     * @param actionEvent login button clicked.
     * @throws SQLException
     * @throws IOException
     */
    public void loginButtonClicked(ActionEvent actionEvent) throws IOException, SQLException {
        Locale locale = Locale.getDefault();
        ResourceBundle languageBundle = ResourceBundle.getBundle("resource/language", locale);

        validateUsernameFieldNotEmpty(loginFormUsernameTF.getText());
        validatePasswordFieldNotEmpty(loginFormPasswordTF.getText());

        try{
            String sqlQuery = "select * from users where User_Name = '" + loginFormUsernameTF.getText() +
                    "' and Password = '" + loginFormPasswordTF.getText() + "'";
            Statement statement = JDBC.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sqlQuery);

            if(rs.next()) {
                loggedUserIDNumber = rs.getInt(1);
               recordUserLoginActivity(true);
                new ChangeCurrentView(actionEvent,"NavigationScreen.fxml");
            }
            else{
                recordUserLoginActivity(false);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(localLanguage.getString("errorDialog"));
                alert.setContentText(localLanguage.getString("invalidCredentialsException"));
                alert.showAndWait();
            }
        }
        catch (SQLException e) {
            System.out.println("There was an error authorizing user against the database.");
        }
        }

    /**
     * This method generates a log of the user's login attempts and records whether they were successful or unsuccessful.
     * Ths method writes the user's login activity and whether it is successful or not to the login_activity.txt file.
     *
     * @param loginSuccessful checks whether the method will attach the string 'LOGIN_SUCCESS' or 'LOGIN_FAIL'.
     */
    private void recordUserLoginActivity(boolean loginSuccessful) {
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(new File("login_activity.txt"), true);

            String loginTimestamp;
            String username;
            String userLoginResult;

            LocalDateTime currentDT = LocalDateTime.now();
            ZonedDateTime utcDT = currentDT.atZone(ZoneId.of("UTC"));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("'['yyyy-MM-dd HH:mm:ss']'");
            loginTimestamp = dateTimeFormatter.format(utcDT);

            username = loginFormUsernameTF.getText();

            if (loginSuccessful) {
                userLoginResult = "LOGIN SUCCESSFUL";
            } else {
                userLoginResult = "LOGIN FAILED";
            }

            String userLoginRecord = loginTimestamp + " UserName:'" + username + "' - " + userLoginResult + "\n";
            fileOutputStream.write(userLoginRecord.getBytes(StandardCharsets.UTF_8));

        } catch (FileNotFoundException exception) {
            System.out.println("The login_activity.txt file was not found" + exception);

        } catch (IOException exception) {
            System.out.println("Exception writing to login_activity.txt " + exception);

        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                System.out.println(" There was an error closing the filestream: " + e);
            }
        }
    }

    /**
     * This method handles the exit button.
     * Displays a message confirming whether the user wants to close the application.
     *
     * @param actionEvent exit button licked.
     */
    public void exitButtonClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(localLanguage.getString("close"));
        alert.setContentText(localLanguage.getString("exitConfirmMessage"));
        alert.showAndWait();

        System.exit(0);
    }
}
