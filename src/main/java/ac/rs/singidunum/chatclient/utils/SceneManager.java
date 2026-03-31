package ac.rs.singidunum.chatclient.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing scene transitions in the application.
 * Caches loaded scenes for better performance.
 */
public class SceneManager {
    private static SceneManager instance;
    private static Stage primaryStage;
    private final Map<String, Scene> sceneCache = new HashMap<>();
    private static final String FXML_PATH = "/ac/rs/singidunum/chatclient/";

    public SceneManager() {}

    /**
     * Get the singleton instance of SceneManager
     */
    public static synchronized SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Initialize the SceneManager with the primary stage
     */
    public static void initialize(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Load and switch to a scene by name (without fixed dimensions)
     * @param sceneName The name of the FXML file (without .fxml extension)
     */
    public void switchScene(String sceneName) {
        switchScene(sceneName, -1, -1);
    }

    /**
     * Load and switch to a scene by name with specified dimensions
     * @param sceneName The name of the FXML file (without .fxml extension)
     * @param width The width of the scene (-1 for automatic sizing)
     * @param height The height of the scene (-1 for automatic sizing)
     */
    public void switchScene(String sceneName, double width, double height) {
        try {
            Scene scene = getScene(sceneName, width, height);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error switching to scene: " + sceneName);
            e.printStackTrace();
        }
    }

    /**
     * Get a scene, loading it if not already cached
     * @param sceneName The name of the FXML file (without .fxml extension)
     * @param width The width of the scene (-1 for automatic sizing)
     * @param height The height of the scene (-1 for automatic sizing)
     * @return The loaded scene
     */
    private Scene getScene(String sceneName, double width, double height) throws IOException {
        String fxmlFile = sceneName + ".fxml";
        Scene scene = sceneCache.get(fxmlFile);

        if (scene == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(
                SceneManager.class.getResource(FXML_PATH + fxmlFile)
            );
            if (width > 0 && height > 0) {
                scene = new Scene(fxmlLoader.load(), width, height);
            } else {
                scene = new Scene(fxmlLoader.load());
            }
            sceneCache.put(fxmlFile, scene);
        }

        return scene;
    }

    /**
     * Clear the scene cache
     */
    public void clearCache() {
        sceneCache.clear();
    }

    /**
     * Get the primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}






