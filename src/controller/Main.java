package controller;

import database.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Sophie Dang.
 */
public class Main extends Application {
    public static void main(String[] args) {
        JDBC.startConnection();
        launch(args);
        JDBC.closeConnection();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginForm.fxml")));
        ResourceBundle language = ResourceBundle.getBundle("resource/language", Locale.getDefault());
        primaryStage.setTitle(language.getString("Title"));
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.show();
    }
}



/*                                                          REFERENCES

    Buchalka,Tim. Java Programming Masterclass Covering Java 11 & Java 17.Udemy.com,https://wgu.udemy.com/course/java-the-complete-java-developer-course/learn/lecture/15386014#overview

    Western Governors University.Software 2 Code Reposition. https://srm--c.na127.visual.force.com/apex/CourseArticle?id=kA03x000000yIQ7CAM&groupId=&searchTerm=&courseCode=C195&rtn=/apex/CommonsExpandedSearch

    Western Governors University.C482 Webinar Blast Archive. Java Programming Webinar Series.https://srm--c.na127.visual.force.com/apex/coursearticle?Id=kA03x000000yIOQCA2

    Western Governors University. C482 Tutorials and Reference Site Links.https://srm--c.na127.visual.force.com/apex/coursearticle?Id=kA03x000000yIOVCA2

    Western Governors University.C195 Webinar Archive.https://srm--c.na127.visual.force.com/apex/coursearticle?Id=kA03x000000yIOpCAM

    Western Governors University.IntelliJ Maven Build FXML and Resource Bundle References.https://srm--c.na127.visual.force.com/apex/CourseArticle?id=kA03x000000okIcCAI&groupId=&searchTerm=&courseCode=C195&rtn=/apex/CommonsExpandedSearch

    Software II: Advanced Java Concepts.https://cgp-oex.wgu.edu/courses/course-v1:WGUx+OEX0022+v02/courseware/c9ce4f8b0b8d4d59b7d29e73150c56a9/0ed71e6e04ca43bca1d5f28d17e97b32/?tpa_hint=oa2-wguid


*/
