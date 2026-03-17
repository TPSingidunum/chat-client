package ac.rs.singidunum.chatclient.configs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class AppConfig {

    private static AppConfig instance;
    private static final String CONFIG_FILE = "client.properties";
    private final Properties properties;

    // Podrazumevane konfiguracije
    private static String DEFAULT_DB_PATH = "db/storage";
    private static String DEFAULT_USER_CERT_PATH = "cert/user.pem";
    private static String DEFAULT_USER_KEY_PATH = "cert/user.key";
    private static String DEFAULT_SERVER_CERT_PATH = "cert/server.pem";
    private static String DEFAULT_WS_PATH = "http://localhost:8080/stomp";

    private AppConfig() {
        properties = new Properties();
        File configFile = new File(CONFIG_FILE);

        if(!configFile.exists()) {
            System.out.println("Pravi se konfig fajl");
            saveDefaults();
        }

        try {
            FileInputStream fis = new FileInputStream(configFile);
            properties.load(fis);
        } catch (IOException e) {
            System.out.printf("Ne mogu ucitati konfiguraciju: " + e.getMessage());
        }
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }

        return instance;
    }

    private void saveDefaults() {
        properties.setProperty("db.path", DEFAULT_DB_PATH);
        properties.setProperty("user.key", DEFAULT_USER_KEY_PATH);
        properties.setProperty("user.cert", DEFAULT_USER_CERT_PATH);
        properties.setProperty("server.cert", DEFAULT_SERVER_CERT_PATH);
        properties.setProperty("app.ws", DEFAULT_WS_PATH);

        saveProperties();
    }

    private void saveProperties() {
        File configFile = new File(CONFIG_FILE);

        try {
            FileOutputStream fos = new FileOutputStream(configFile);
            properties.store(fos, "Chat Application - Singidunum");
        } catch (IOException e) {
            System.out.printf("Ne mogu sacuvati konfiguraciju: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        return (String) properties.getOrDefault(key, null);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }
}
