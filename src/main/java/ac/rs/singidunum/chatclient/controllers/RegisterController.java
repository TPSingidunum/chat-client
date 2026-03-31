package ac.rs.singidunum.chatclient.controllers;

import ac.rs.singidunum.chatclient.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    public TextField usernameField;
    public TextField emailField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public Label messageLabel;

    public void onRegisterClick(ActionEvent actionEvent) {
        // Generisem User certificate
        // Generisem bazu
        // Onemogucim registraciju, i postavim da podrazumevano otravaram trenutnog korisnika
    }

    public void onClearClick(ActionEvent actionEvent) {
    }

    public void onBackClick(ActionEvent actionEvent) {
        SceneManager.getInstance().switchScene("login-view");
    }
}
