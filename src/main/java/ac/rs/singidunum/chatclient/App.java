package ac.rs.singidunum.chatclient;

import ac.rs.singidunum.chatclient.configs.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        // Config Service
//        AppConfig appConfig = AppConfig.getInstance();
//        System.out.println("DB: " + appConfig.getProperty("db.path"));
//        appConfig.setProperty("db.pass", "Teodor");

        stage.show();
    }
}
