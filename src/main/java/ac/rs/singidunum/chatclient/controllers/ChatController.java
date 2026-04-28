package ac.rs.singidunum.chatclient.controllers;

import ac.rs.singidunum.chatclient.configs.AppConfig;
import ac.rs.singidunum.chatclient.configs.DbConfig;
import ac.rs.singidunum.chatclient.messaging.ConnectionState;
import ac.rs.singidunum.chatclient.messaging.SubscriptionHandle;
import ac.rs.singidunum.chatclient.services.ChatService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class ChatController {
    public Label statusLabel;
    public Button logoutButton;
    public ListView<String> conversationList;
    public ListView<String> messageList;
    public TextField messageInput;

    private DbConfig dbConfig;
    private AppConfig appConfig;
    private ChatService chatService;
    private ChangeListener<ConnectionState> connectionState;

    // Subscription Channels
    private SubscriptionHandle userSubscription;


    @FXML
    public void initialize() {
        dbConfig = DbConfig.getInstance();
        appConfig = AppConfig.getInstance();
        chatService = ChatService.getInstance();

        connectionState = ((observable, oldValue, newValue) -> {
            statusLabel.setText(newValue.toString());
        });

        chatService.connectionStateProperty().addListener(connectionState);
        statusLabel.setText(chatService.getConnectionState().name());

        conversationList.setItems(FXCollections.observableArrayList("Loading users"));

        // Subscribe to channels
        userSubscription = chatService.subscribeToUsers(
                this::updateConversationList,
                throwable -> {
                    throwable.printStackTrace();

                    Platform.runLater(() -> {
                        conversationList.setItems(FXCollections.observableArrayList("Error loading users"));
                    });
                }
        );

    }

    private void updateConversationList(List<String> activeUsers) {
        System.out.println("Update is happening");
        System.out.println("USERS: " + activeUsers.toString());

        Platform.runLater(() -> {
            conversationList.setItems(FXCollections.observableArrayList(activeUsers));
        });
    }

    public void onSendClick(ActionEvent actionEvent) {
    }

    public void onLogoutAction(ActionEvent actionEvent) {
    }
}
