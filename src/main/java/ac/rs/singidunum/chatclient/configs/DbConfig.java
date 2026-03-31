package ac.rs.singidunum.chatclient.configs;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.sqlite.mc.SQLiteMCChacha20Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConfig {

    private static DbConfig instance;
    private AppConfig appConfig = AppConfig.getInstance();
    private Connection connection;
    private DSLContext dsl;
    private boolean isInitialized = false;

    public static DbConfig getInstance() {
        if (instance == null) {
            instance = new DbConfig();
        }
        return instance;
    }

    public void initializeDb(String password, Path path) {
        try {
            String url = "jdbc:sqlite:" + path.toString();

            Properties pros = SQLiteMCChacha20Config
                    .getDefault()
                    .withKey(password)
                    .build().toProperties();

            connection = DriverManager.getConnection(url, pros);

            dsl = DSL.using(connection, SQLDialect.DEFAULT);

            createDB();

        } catch (SQLException e) {
            System.out.println("Greska prilikom inicializacije baze");
            //throw new RuntimeException(e);
        }
    }

    public void connect(String password) {
        try {
            String url = "jdbc:sqlite:" + appConfig.getDbPath();

            Properties pros = SQLiteMCChacha20Config
                    .getDefault()
                    .withKey(password)
                    .build().toProperties();

            connection = DriverManager.getConnection(url, pros);

            dsl = DSL.using(connection, SQLDialect.DEFAULT);

            isInitialized = true;
        } catch (SQLException e) {
            System.out.println("Greska prilikom konekcije na bazu");
            //throw new RuntimeException(e);
        }
    }

    private void createDB() {
        dsl.execute("""
                CREATE TABLE IF NOT EXISTS messages (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    content TEXT NOT NULL
                )
                """);
    }

    public void validateConnection() {
        dsl.fetch("SELECT 1");
    }

    public void close() {
        try {
            if ( connection != null && !connection.isClosed()) {
                connection.close();
                isInitialized = false;
                System.out.println("Database Closed");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public DSLContext dsl() {
        return dsl;
    }
}
