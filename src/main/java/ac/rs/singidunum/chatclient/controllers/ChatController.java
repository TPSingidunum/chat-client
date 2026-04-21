package ac.rs.singidunum.chatclient.controllers;

import ac.rs.singidunum.chatclient.configs.AppConfig;
import ac.rs.singidunum.chatclient.configs.DbConfig;
import ac.rs.singidunum.chatclient.services.ChatService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController {
    public Label statusLabel;
    public Button logoutButton;
    public ListView conversationList;
    public ListView messageList;
    public TextField messageInput;

    private DbConfig dbConfig;
    private AppConfig appConfig;
    private ChatService chatService;


    @FXML
    public void initialize() {
        dbConfig = DbConfig.getInstance();
        appConfig = AppConfig.getInstance();
        chatService = ChatService.getInstance();

        System.out.println("Logged in user: " + appConfig.getUsername());
    }

    public void onSendClick(ActionEvent actionEvent) {
    }

    public void onLogoutAction(ActionEvent actionEvent) {
    }
}
