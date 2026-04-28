package ac.rs.singidunum.chatclient.messaging.dtos;

public class SendMessageResponse {
    private String payload;
    private String from;

    public SendMessageResponse() {
    }

    public SendMessageResponse(String payload, String from) {
        this.payload = payload;
        this.from = from;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return this.from + ": " + this.payload;
    }
}
