package ac.rs.singidunum.chatclient.controllers;

import ac.rs.singidunum.chatclient.configs.DbConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class LoginController {

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
}
