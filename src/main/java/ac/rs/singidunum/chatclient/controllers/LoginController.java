package ac.rs.singidunum.chatclient.controllers;

import ac.rs.singidunum.chatclient.configs.AppConfig;
import ac.rs.singidunum.chatclient.configs.DbConfig;
import ac.rs.singidunum.chatclient.database.dtos.UserProfile;
import ac.rs.singidunum.chatclient.messaging.ConnectionState;
import ac.rs.singidunum.chatclient.messaging.dtos.LoginRequest;
import ac.rs.singidunum.chatclient.messaging.dtos.LoginResponse;
import ac.rs.singidunum.chatclient.services.ChatService;
import ac.rs.singidunum.chatclient.utils.SceneManager;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class LoginController {
    public Label errorLabel;
    public Label statusLabel;
    public Button loginButton;

    private DbConfig dbConfig;
    private AppConfig appConfig;
    private ChatService chatService;
    private ChangeListener<ConnectionState> connectionState;

    @FXML
    public PasswordField passwordField;

    @FXML
    public void initialize() {
        dbConfig = DbConfig.getInstance();
        appConfig = AppConfig.getInstance();
        chatService = ChatService.getInstance();

        connectionState = ((observable, oldValue, newValue) -> {
           statusLabel.setText(newValue.toString());
           loginButton.setDisable(newValue == ConnectionState.CONNECTING);
        });

        chatService.connectionStateProperty().addListener(connectionState);
        statusLabel.setText(chatService.getConnectionState().name());
    }

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

        // Username dohvatio iz baze
        UserProfile userProfile = DbConfig.getInstance().getUserProfile();
        LoginRequest lr = new LoginRequest(userProfile.getUsername());
        chatService.connect(appConfig.getProperty("app.ws"))
                .thenCompose(ignore -> chatService.login(lr))
                .thenAccept(result -> handleLoginResponse(result, userProfile))
                .exceptionally(error -> {
                    dbConfig.close();

                    Platform.runLater(() -> {
                        errorLabel.setText("Error could not connect to server");
                    });
                    return null;
                });
    }

    public void handleLoginResponse(LoginResponse response, UserProfile profile) {
        // Uradim jos neku logiku za potvrdu identiteta
        // Proveravamo da li treba poslati serveru jos neke kljuceve
        // Dodatne operacije na lokalnom nivou

        appConfig.setUsername(profile.getUsername());
        appConfig.setEmail(profile.getEmail());

        Platform.runLater(() -> {
            SceneManager.getInstance().switchScene("chat-view");
        });
    }

    public void onClearClick(ActionEvent actionEvent) {
    }

    public void onRegisterClick(ActionEvent actionEvent) {
        SceneManager.getInstance().switchScene("register-view");
    }
}
