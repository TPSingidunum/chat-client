package com.masofino.birp.chatclient.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private static final String CONFIG_FILE = "chatclient.properties";
    private static AppConfig instance;
    private final Properties properties;

    // Default config values
    private static final String DEFAULT_CERT_PATH = "certs/user_cert.pem";
    private static final String DEFAULT_KEY_PATH = "certs/user_key.pem";
    private static final String DEFAULT_API_URL = "http://localhost:8080/api/v1/";
    
    private AppConfig() {
        properties = new Properties();
        File configFile = new File(CONFIG_FILE);
        
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("Error loading config file: " + e.getMessage());
                setDefaults();
            }
        } else {
            setDefaults();
            saveConfig();
        }
    }
    
    private void setDefaults() {
        properties.setProperty("certificate.path", DEFAULT_CERT_PATH);
        properties.setProperty("privatekey.path", DEFAULT_KEY_PATH);
        properties.setProperty("api.url", DEFAULT_API_URL);
    }
    
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
    
    public String getCertificatePath() {
        return properties.getProperty("certificate.path", DEFAULT_CERT_PATH);
    }
    
    public String getPrivateKeyPath() {
        return properties.getProperty("privatekey.path", DEFAULT_KEY_PATH);
    }
    
    public String getApiUrl() {
        return properties.getProperty("api.url", DEFAULT_API_URL);
    }
    
    public void setCertificatePath(String path) {
        properties.setProperty("certificate.path", path);
        saveConfig();
    }
    
    public void setPrivateKeyPath(String path) {
        properties.setProperty("privatekey.path", path);
        saveConfig();
    }
    
    public void setApiUrl(String url) {
        properties.setProperty("api.url", url);
        saveConfig();
    }
    
    public void saveConfig() {
        try {
            File configFile = new File(CONFIG_FILE);
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                properties.store(fos, "Chat Client Configuration");
            }
        } catch (IOException e) {
            System.err.println("Error saving config file: " + e.getMessage());
        }
    }
}