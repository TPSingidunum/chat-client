package ac.rs.singidunum.chatclient;

import ac.rs.singidunum.chatclient.configs.AppConfig;
import ac.rs.singidunum.chatclient.configs.DbConfig;
import ac.rs.singidunum.chatclient.utils.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login View");
        stage.setScene(scene);

        SceneManager.getInstance();
        SceneManager.initialize(stage);

        // Config Service
        AppConfig appConfig = AppConfig.getInstance();
        DbConfig db = DbConfig.getInstance();

        stage.show();
    }
}
