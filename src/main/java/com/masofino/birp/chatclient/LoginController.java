package com.masofino.birp.chatclient;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerNavigationButton;

    @FXML
    private Label errorMessageLabel;

    @FXML
    protected void onLoginButtonClick() {
        // Login logic will be implemented later
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorMessageLabel.setText("Username and password are required!");
            return;
        }

        // TODO: Implement actual login logic

        // For now, just navigate to the chat view
        try {
            HelloApplication.navigateTo(loginButton, "chat-view", "Chat Client", 800, 600);
        } catch (IOException e) {
            errorMessageLabel.setText("Error loading chat view!");
            e.printStackTrace();
        }
    }

    @FXML
    protected void onRegisterNavigationClick() {
        try {
            HelloApplication.navigateTo(registerNavigationButton, "register-view", "Chat Client - Register", 400, 500);
        } catch (IOException e) {
            errorMessageLabel.setText("Error loading register view!");
            e.printStackTrace();
        }
    }
}