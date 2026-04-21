package ac.rs.singidunum.chatclient.messaging.dtos;

public class LoginResponse {
    private String result;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(String result, String token) {
        this.result = result;
        this.token = token;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
