package ac.rs.singidunum.chatclient.messaging.dtos;

public class LoginResponse {
    private String result;
    private String text;

    public LoginResponse() {
    }

    public LoginResponse(String result, String text) {
        this.result = result;
        this.text = text;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "result='" + result + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
