package com.masofino.birp.chatclient.websocket;

import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class WebSocketService {
    private static WebSocketService INSTANCE;

    private final OkHttpClient client;
    private WebSocket socket;
    private final List<WebSocketListener> listeners = new CopyOnWriteArrayList<>();

    private WebSocketService() {
        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static synchronized WebSocketService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WebSocketService();
        }
        return INSTANCE;
    }

    /**
     * Connects to the given URL (ws:// or wss://).  If authToken is non‑null,
     * it will be sent in an Authorization header.
     */
    public void connect(String wsUrl, String authToken) {
        Request.Builder reqB = new Request.Builder().url(wsUrl);

        if (authToken != null && !authToken.isEmpty()) {
            reqB.addHeader("Authorization", "Bearer " + authToken);
        }

        Request request = reqB.build();

        // tear down any existing
        if (socket != null) {
            socket.cancel();
        }

        socket = client.newWebSocket(request, new okhttp3.WebSocketListener() {
            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                Platform.runLater(() ->
                        listeners.forEach(WebSocketListener::onOpen)
                );
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                Platform.runLater(() ->
                        listeners.forEach(l -> l.onMessage(text))
                );
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
                Platform.runLater(() ->
                        listeners.forEach(l -> l.onError(t))
                );
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                webSocket.close(code, reason);
                Platform.runLater(() ->
                        listeners.forEach(l -> l.onClose(code, reason))
                );
            }

            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                Platform.runLater(() ->
                        listeners.forEach(l -> l.onClose(code, reason))
                );
            }
        });

        // The OkHttpClient will handle threading for you.
    }

    /** Send a text message to the server. */
    public void send(String text) {
        if (socket != null) {
            socket.send(text);
        }
    }

    /** Gracefully close. */
    public void close(int code, String reason) {
        if (socket != null) {
            socket.close(code, reason);
        }
    }

    /** Subscribe a listener (usually in your controller's initialize()). */
    public void addListener(WebSocketListener listener) {
        listeners.add(listener);
    }

    /** Unsubscribe (in your controller's cleanup). */
    public void removeListener(WebSocketListener listener) {
        listeners.remove(listener);
    }
}
