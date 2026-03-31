package ac.rs.singidunum.chatclient.controllers;

import ac.rs.singidunum.chatclient.configs.DbConfig;
import ac.rs.singidunum.chatclient.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    public TextField usernameField;
    private DbConfig dbConfig = DbConfig.getInstance();

    @FXML
    public PasswordField passwordField;

    @FXML
    private Label welcomeText;

    @FXML
    protected void login() {
        if (passwordField.getText().isEmpty()) {
            welcomeText.setText("Nisi uneo sifru");
        }

        System.out.println("Sifra koriska: " + passwordField.getText());
        dbConfig.initializeDb(passwordField.getText());

    }

    public void onLoginClick(ActionEvent actionEvent) {
    }

    public void onClearClick(ActionEvent actionEvent) {
    }

    public void onRegisterClick(ActionEvent actionEvent) {
        SceneManager.getInstance().switchScene("register-view");
    }
}
