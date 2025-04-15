package com.masofino.birp.chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 400, 500);
        stage.setTitle("Chat Client - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Utility method to navigate between views
     *
     * @param sourceControl Any control from the current scene
     * @param viewName Name of the FXML file to load (without .fxml extension)
     * @param title Window title for the new view
     * @param width Width of the new scene
     * @param height Height of the new scene
     * @throws IOException If the view cannot be loaded
     */
    public static void navigateTo(Control sourceControl, String viewName, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(viewName + ".fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) sourceControl.getScene().getWindow();
        Scene scene = new Scene(root, width, height);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}