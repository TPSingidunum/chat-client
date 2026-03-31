package ac.rs.singidunum.chatclient.controllers;

import ac.rs.singidunum.chatclient.configs.DbConfig;
import ac.rs.singidunum.chatclient.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    public Label errorLabel;
    private DbConfig dbConfig = DbConfig.getInstance();

    @FXML
    public PasswordField passwordField;

    public void onLoginClick(ActionEvent actionEvent) {
        if (passwordField.getText().isEmpty()) {
            errorLabel.setText("Password is empty");
        }

        dbConfig.connect(passwordField.getText());
        try {
            dbConfig.validateConnection();
        } catch (Exception e) {
            errorLabel.setText("Wrong password");
            return;
        }

        SceneManager.getInstance().switchScene("chat-view");
    }

    public void onClearClick(ActionEvent actionEvent) {
    }

    public void onRegisterClick(ActionEvent actionEvent) {
        SceneManager.getInstance().switchScene("register-view");
    }
}
