package ac.rs.singidunum.chatclient.messaging;

public enum StompDestination {
    APP_AUTH_LOGIN("/app/auth.login"),
    USER_QUEUE_AUTH("/user/queue/auth"),
    USER_CONNECTED("/app/users.connected"),
    CHAT_SEND("/app/chat.send"),
    TOPIC_USERS("/topic/users")
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
