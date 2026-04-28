package ac.rs.singidunum.chatclient.controllers;

import ac.rs.singidunum.chatclient.configs.AppConfig;
import ac.rs.singidunum.chatclient.configs.DbConfig;
import ac.rs.singidunum.chatclient.messaging.ConnectionState;
import ac.rs.singidunum.chatclient.messaging.StompDestination;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private SubscriptionHandle topicUsersSubscription;
    private SubscriptionHandle userQueueConnectedSubscription;

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
        topicUsersSubscription = chatService.subscribeToTopicUsers(
                this::updateConversationList,
                throwable -> {
                    throwable.printStackTrace();

                    Platform.runLater(() -> {
                        conversationList.setItems(FXCollections.observableArrayList("Error loading users"));
                    });
                }
        );

        userQueueConnectedSubscription = chatService.subscribeToUserQueueConnected(
                this::updateConversationList,
                throwable -> {
                    throwable.printStackTrace();

                    Platform.runLater(() -> {
                        conversationList.setItems(FXCollections.observableArrayList("Error loading users"));
                    });
                }
        );

        getConnectedUsers();
        //CompletableFuture.delayedExecutor(1000, TimeUnit.MILLISECONDS).execute(this::getConnectedUsers);
    }

    private void getConnectedUsers() {
        try {
            chatService.send(StompDestination.APP_USERS_CONNECTED.toString(), Map.of());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching users");
        }
    }

    private void updateConversationList(List<String> activeUsers) {
        System.out.println("Update is happening");
        System.out.println("USERS: " + activeUsers.toString());
        System.out.println("Active user: " + appConfig.getUsername());

        List<String> newUsers = new ArrayList<>(activeUsers.stream()
                .filter(item -> !item.equals(appConfig.getUsername()))
                .toList());

        if (newUsers.isEmpty()) {
            newUsers.add("No active users");
        }

        Platform.runLater(() -> {
            conversationList.setItems(FXCollections.observableArrayList(newUsers));
        });
    }

    public void onSendClick(ActionEvent actionEvent) {
    }

    public void onLogoutAction(ActionEvent actionEvent) {
    }
}
