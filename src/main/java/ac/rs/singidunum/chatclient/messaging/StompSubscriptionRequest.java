package ac.rs.singidunum.chatclient.messaging;

import java.util.function.Consumer;

public class StompSubscriptionRequest<T> {
    private final String id;
    private final String destination;
    private final Class<T> payloadType;
    private final Consumer<T> onMessage;
    private final Consumer<Throwable> onError;

    public StompSubscriptionRequest(String id, String destination, Class<T> payloadType, Consumer<T> onMessage, Consumer<Throwable> onError) {
        this.id = id;
        this.destination = destination;
        this.payloadType = payloadType;
        this.onMessage = onMessage;
        this.onError = onError;
    }

    public String getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }

    public Class<T> getType() {
        return payloadType;
    }

    public Consumer<T> getOnMessage() {
        return onMessage;
    }

    public Consumer<Throwable> getOnError() {
        return onError;
    }
}
