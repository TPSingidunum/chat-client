package ac.rs.singidunum.chatclient.configs;

public class StompConfig {

    private static StompConfig instance;
    private AppConfig appConfig = AppConfig.getInstance();
    private boolean isConnected = false;

    public static StompConfig getInstance() {
        if (instance == null) {
            instance = new StompConfig();
        }
        return instance;
    }
}
