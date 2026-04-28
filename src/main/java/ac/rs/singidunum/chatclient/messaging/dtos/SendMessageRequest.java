package ac.rs.singidunum.chatclient.messaging.dtos;

public class SendMessageRequest {
    private String payload;

    public SendMessageRequest() {
    }

    public SendMessageRequest(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
