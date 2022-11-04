package controller;

import javafx.event.ActionEvent;
import utilities.ChangeCurrentView;

import java.io.IOException;

/**
 *This controller class provides logic for the navigation screen.
 *
 * @author Sophie Dang.
 */

public class NavigationScreenController {

    /**
     * This method handles the first button on the screen.
     * This button takes the user to the main screen form.
     *
     * @param actionEvent apptsAndCustsTableViewButton.
     */
    public void navApptsAndCustsTableViewButtonClicked(ActionEvent actionEvent) throws IOException {
        new ChangeCurrentView(actionEvent,"MainScreenViews.fxml");
    }

    /**
     *This method handles the reports button.
     *This button takes the user to the reports form screen.
     *
     * @param actionEvent reports button clicked.
     */
    public void navReportsButtonClicked(ActionEvent actionEvent) throws IOException {
        new ChangeCurrentView(actionEvent,"MainReportsScreen.fxml");
    }

    /**
     * This method handles the login form button.
     * This button takes the user back to the login form.
     *
     * @param actionEvent login button clicked.
     */
    public void navLogoutButtonClicked(ActionEvent actionEvent) throws IOException {
        new ChangeCurrentView(actionEvent,"LoginForm.fxml");
    }
}
