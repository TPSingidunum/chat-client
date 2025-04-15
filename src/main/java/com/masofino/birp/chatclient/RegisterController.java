package com.masofino.birp.chatclient;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginNavigationButton;

    @FXML
    private Label errorMessageLabel;

    @FXML
    protected void onRegisterButtonClick() {
        // Registration logic will be implemented later
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessageLabel.setText("All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessageLabel.setText("Passwords do not match!");
            return;
        }

        // TODO: Implement actual registration logic

        // For now, just navigate to the login view
        try {
            HelloApplication.navigateTo(registerButton, "login-view", "Chat Client - Login", 400, 500);
        } catch (IOException e) {
            errorMessageLabel.setText("Error loading login view!");
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLoginNavigationClick() {
        try {
            HelloApplication.navigateTo(loginNavigationButton, "login-view", "Chat Client - Login", 400, 500);
        } catch (IOException e) {
            errorMessageLabel.setText("Error loading login view!");
            e.printStackTrace();
        }
    }
}