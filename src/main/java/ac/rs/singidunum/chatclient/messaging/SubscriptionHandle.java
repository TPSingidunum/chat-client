package ac.rs.singidunum.chatclient.messaging;

@FunctionalInterface
public interface SubscriptionHandle {
    void unsubscribe();
}
