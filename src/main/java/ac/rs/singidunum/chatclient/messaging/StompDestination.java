package ac.rs.singidunum.chatclient.messaging;

public enum StompDestination {
    APP_AUTH_LOGIN("/app/auth.login"),
    USER_QUEUE_AUTH("/user/queue/auth"),
    USER_QUEUE_CONNECTED("/user/queue/connected-users"),
    APP_USERS_CONNECTED("/app/users.connected"),
    APP_CHAT_SEND("/app/chat.send"),
    TOPIC_USERS("/topic/users"),
    TOPIC_CHAT("/topic/chat")
    ;

    private final String text;

    StompDestination(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
