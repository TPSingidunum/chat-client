package ac.rs.singidunum.chatclient.messaging.dtos;

public class LoginResponse {
    private boolean result;
    private String text;

    public LoginResponse() {
    }

    public LoginResponse(boolean result, String text) {
        this.result = result;
        this.text = text;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
