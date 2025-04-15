package com.masofino.birp.chatclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ChatController {
    @FXML
    private Label chatTitleLabel;

    @FXML
    private ListView<String> messageListView;

    @FXML
    private ListView<String> userListView;

    @FXML
    private TextField messageField;

    @FXML
    private Button sendButton;

    @FXML
    private Button logoutButton;

    // Example data (will be replaced with actual data later)
    private ObservableList<String> messages = FXCollections.observableArrayList();
    private ObservableList<String> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize with some example data
        users.addAll("Alice", "Bob", "Charlie", "David", "Eva");
        messages.addAll("Welcome to the chat room!", "Please be respectful to other users.");

        userListView.setItems(users);
        messageListView.setItems(messages);
    }

    @FXML
    protected void onSendButtonClick() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            // TODO: Implement actual message sending logic
            messages.add("You: " + message);
            messageField.clear();
        }
    }

    @FXML
    protected void onLogoutButtonClick() {
        // TODO: Implement actual logout logic
        try {
            HelloApplication.navigateTo(logoutButton, "login-view", "Chat Client - Login", 400, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}