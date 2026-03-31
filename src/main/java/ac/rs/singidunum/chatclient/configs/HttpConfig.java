package ac.rs.singidunum.chatclient.configs;

public class HttpConfig {
    private static HttpConfig instance;
    private AppConfig appConfig = AppConfig.getInstance();

    public static HttpConfig getInstance() {
        if (instance == null) {
            instance = new HttpConfig();
        }
        return instance;
    }
}
