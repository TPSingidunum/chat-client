package com.masofino.birp.chatclient.controllers;

import com.masofino.birp.chatclient.HelloApplication;
import com.masofino.birp.chatclient.api.*;
import com.masofino.birp.chatclient.security.CertificateManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class RegisterController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

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
    protected void onRegisterButtonClick() throws Exception {
        errorMessageLabel.setText("");

        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessageLabel.setText("All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessageLabel.setText("Passwords do not match.");
            return;
        }

        registerButton.setDisable(true);
        errorMessageLabel.setText("Registering, please wait...");

        CertificateManager.generateCertificate(username);
        String certificate = CertificateManager.getCertificateAsString();

        //Get token
        CompletableFuture<ApiResult<OtpResponse>> otpFuture = ApiClient.getOtp();
        ApiResult<OtpResponse> apiOtp = otpFuture.join();

        if (!apiOtp.isSuccessful()){
           errorMessageLabel.setText(apiOtp.getRawBody());
           registerButton.setDisable(false);
           return;
        }

        RegistrationRequest userRegister = new RegistrationRequest(username,email, certificate);
        CompletableFuture<ApiResult<RegistrationResponse>> registerFuture = ApiClient.register(apiOtp.getData().getToken(), userRegister);
        ApiResult<RegistrationResponse> apiRegister = registerFuture.join();

        if (!apiRegister.isSuccessful()){
            errorMessageLabel.setText(apiRegister.getRawBody());
            registerButton.setDisable(false);
        } else {
            HelloApplication.navigateTo(registerButton, "login-view", "Login", 800, 600);
        }
    }

    @FXML
    protected void onLoginNavigationClick() {
        try {
            HelloApplication.navigateTo(loginNavigationButton, "login-view", "Login", 600, 400);
        } catch (IOException e) {
            errorMessageLabel.setText("Error navigating to login: " + e.getMessage());
        }
    }

    private void handleError(String message) {
        errorMessageLabel.setText(message);
    }
}