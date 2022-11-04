package utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Sophie Dang.
 */
public class ChangeCurrentView {

    /**
     * This method is created to reduce redundant code that needs to be written whenever a view needs to be changed.
     */
        private static final String rootName = "/view/";

        public ChangeCurrentView(javafx.event.ActionEvent actionEvent, String viewName) throws IOException {
            String completeViewName = rootName + viewName;
            Parent root = FXMLLoader.load(Objects.requireNonNull(ChangeCurrentView.class.getResource(completeViewName)));
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
}
