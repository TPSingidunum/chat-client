package ac.rs.singidunum.chatclient.messaging;

public enum StompDestination {
    USER_LOGIN("/app/auth.login"),
    USER_CONNECTED("/app/users.connected"),
    CHAT_SEND("/app/chat.send"),
    ;

    private final String text;

    StompDestination(String text) {
        this.text = text;
    }
}
