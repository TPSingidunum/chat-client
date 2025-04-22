package com.masofino.birp.chatclient.websocket;

public interface WebSocketListener {
    /**
     * Called on the JavaFX Application thread whenever a text message arrives.
     * @param text the raw JSON/text payload from the server
     */
    void onMessage(String text);

    /** Called on the JavaFX Application thread if the socket fails. */
    default void onError(Throwable t) {}

    /** Called on the JavaFX Application thread when the socket opens. */
    default void onOpen() {}

    /** Called on the JavaFX Application thread when the socket closes. */
    default void onClose(int code, String reason) {}
}