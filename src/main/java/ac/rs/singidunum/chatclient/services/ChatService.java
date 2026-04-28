package ac.rs.singidunum.chatclient.services;

import ac.rs.singidunum.chatclient.configs.AppConfig;
import ac.rs.singidunum.chatclient.messaging.ConnectionState;
import ac.rs.singidunum.chatclient.messaging.StompDestination;
import ac.rs.singidunum.chatclient.messaging.StompManager;
import ac.rs.singidunum.chatclient.messaging.SubscriptionHandle;
import ac.rs.singidunum.chatclient.messaging.dtos.ActiveUsers;
import ac.rs.singidunum.chatclient.messaging.dtos.LoginRequest;
import ac.rs.singidunum.chatclient.messaging.dtos.LoginResponse;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ChatService {

    private StompManager stompManager;
    private static ChatService instance;
    private final ObjectProperty<ConnectionState> connectionState = new SimpleObjectProperty<>(ConnectionState.DISCONNECTED);

    private ChatService() {
        this.stompManager =  new StompManager();
    }

    public static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService();
        }

        return instance;
    }

    public CompletableFuture<Void> connect(String url) {
        setConnectionState(ConnectionState.CONNECTING);
        return stompManager.connect(url)
                .whenComplete((result,error) -> {
                   if (error == null) {
                       System.out.println("Connect: " + url);
                       setConnectionState(ConnectionState.CONNECTED);
                   } else {
                       System.out.println("Not connected: " + url);
                       setConnectionState(ConnectionState.ERROR);
                   }
                });
    }

    public CompletableFuture<LoginResponse> login(LoginRequest data) {
        CompletableFuture<LoginResponse> result = new CompletableFuture<>();
        AtomicReference<SubscriptionHandle> handleRef = new AtomicReference<>();

        handleRef.set(stompManager.subscribe(
                StompDestination.USER_QUEUE_AUTH.toString(),
                LoginResponse.class,
                response -> {
                    unsubscribe(handleRef);
                    result.complete(response);
                },
                error -> {
                    unsubscribe(handleRef);
                    result.completeExceptionally(error);
                }
        ));

        try {
            stompManager.send(StompDestination.APP_AUTH_LOGIN.toString(), data);
        } catch (Exception e) {
            unsubscribe(handleRef);
            result.completeExceptionally(e);
        }

        return result;
    }

    public SubscriptionHandle subscribeToTopicUsers(
            Consumer<List<String>> onMessage,
            Consumer<Throwable> onError
    ) {
        return stompManager.subscribe(
                StompDestination.TOPIC_USERS.toString(),
                List.class,
                payload -> onMessage.accept(payload.stream().map(String::valueOf).toList()),
                onError
        );
    }

    public SubscriptionHandle subscribeToUserQueueConnected(
            Consumer<List<String>> onMessage,
            Consumer<Throwable> onError
    ) {
        return stompManager.subscribe(
                StompDestination.USER_QUEUE_CONNECTED.toString(),
                List.class,
                payload -> onMessage.accept(payload.stream().map(String::valueOf).toList()),
                onError
        );
    }

    public void send(String destination, Object payload) {
        stompManager.send(destination, payload);
    }

    public void setConnectionState (ConnectionState state) {
        if (Platform.isFxApplicationThread()) {
            connectionState.set(state);
        } else {
            Platform.runLater(() -> {
                connectionState.set(state);
            });
        }
    }

    private void unsubscribe(AtomicReference<SubscriptionHandle> handleRef) {
        SubscriptionHandle handle = handleRef.getAndSet(null);
        if (handle != null) {
            handle.unsubscribe();
        }
    }

    public ObjectProperty<ConnectionState> connectionStateProperty() {
        return connectionState;
    }

    public ConnectionState getConnectionState() {
        return connectionState.get();
    }


}
