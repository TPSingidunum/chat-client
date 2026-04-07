package ac.rs.singidunum.chatclient.services;

import ac.rs.singidunum.chatclient.messaging.StompManager;

public class ChatService {

    private StompManager stompManager;

    public ChatService(StompManager stompManager) {
        this.stompManager = stompManager;
    }


}
