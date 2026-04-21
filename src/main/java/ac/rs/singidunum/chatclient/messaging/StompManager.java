package ac.rs.singidunum.chatclient.messaging;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class StompManager {

    private final WebSocketStompClient stompClient;
    private final ExecutorService executorService;
    private final ObjectProperty<ConnectionState> connectionState = new SimpleObjectProperty<>(ConnectionState.DISCONNECTED);
    private final ConcurrentMap<String, StompSession.Subscription> liveSubscriptions = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, StompSubscriptionRequest<?>> desiredSubscriptions = new ConcurrentHashMap<>();

    private volatile StompSession session;

    public StompManager() {
        this.stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        this.executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "stomp-thread");
            thread.setDaemon(true);
            return thread;
        });
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

    public CompletableFuture<Void> connect(String url) {
        setConnectionState(ConnectionState.CONNECTING);
        CompletableFuture<Void> result = new CompletableFuture<>();

        stompClient.connectAsync(url, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession stopSession, StompHeaders connectedHeaders) {
                session = stopSession;
                setConnectionState(ConnectionState.CONNECTED);
                restoreSubscriptions();
                result.complete(null);
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                clearSession(ConnectionState.ERROR);
                if (!result.isDone()) {
                    result.completeExceptionally(exception);
                }
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                clearSession(ConnectionState.ERROR);
                if (!result.isDone()) {
                    result.completeExceptionally(exception);
                }
            }
        }).whenComplete((connectedSession, throwable) -> {
            if (throwable != null && !result.isDone()) {
                clearSession(ConnectionState.ERROR);
                result.completeExceptionally(throwable);
            }
        });

        return result;
    }

    public <T> SubscriptionHandle subscribe(
            String destination,
            Class<T> payloadType,
            Consumer<T> onMessage,
            Consumer<Throwable> onError
    ) {
        String id = UUID.randomUUID().toString();
        StompSubscriptionRequest<T> request = new StompSubscriptionRequest<>(
                id,
                destination,
                payloadType,
                onMessage,
                onError
        );

        desiredSubscriptions.put(id,request);

        if (isConnected()) {
            createLiveSubscription(request);
        }

        return () -> unsubscribe(id);
    }

    private <T> void createLiveSubscription(StompSubscriptionRequest<T> request) {
        if (!isConnected()) {
            return;
        }

        StompSession.Subscription subscription = session.subscribe(
                request.getDestination(),
                new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return request.getType();
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        executorService.submit(() -> {
                            try {
                                T typedPayload = request.getType().cast(payload);
                                Platform.runLater(() -> request.getOnMessage().accept(typedPayload));
                            } catch (Exception e) {
                                Consumer<Throwable> onError = request.getOnError();
                                if (onError != null) {
                                    Platform.runLater(() -> onError.accept(e));
                                }
                            }
                        });
                    }
                }
        );

        liveSubscriptions.put(request.getId(), subscription);
    }

    public void send(String destination, Object payload) {
        if (!isConnected()) {
            throw new IllegalStateException("STOMP is not connected.");
        }
        session.send(destination, payload);
    }

    public void unsubscribe(String id) {
        desiredSubscriptions.remove(id);
        StompSession.Subscription liveSubscription = liveSubscriptions.remove(id);
        if (liveSubscription != null) {
            try {
                liveSubscription.unsubscribe();
            } catch (Exception ignored) {
            }
        }
    }

    public boolean isConnected() {
        StompSession currentSession = session;
        return currentSession != null && currentSession.isConnected();
    }

    private void clearSession(ConnectionState state) {
        session = null;
        liveSubscriptions.clear();
        setConnectionState(state);
    }

    @SuppressWarnings("unchecked")
    private void restoreSubscriptions() {
        desiredSubscriptions.values().forEach(
                request -> createLiveSubscription((StompSubscriptionRequest<Object>) request)
        );
    }

}