package ac.rs.singidunum.chatclient.configs;

import ac.rs.singidunum.chatclient.database.dtos.UserProfile;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.sqlite.mc.SQLiteMCChacha20Config;

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

        //wp-options
        dsl.execute("""
                CREATE TABLE IF NOT EXISTS profile (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    email TEXT NOT NULL
                )
                """);
    }

    // User DB queriesS

    public void createUserProfile(String username, String email) {
        var query = dsl.insertInto(
            DSL.table("profile"),
                DSL.field("username"),
                DSL.field("email")
        );

        query.values(username, email).execute();
    }

    public UserProfile getUserProfile() {
       return dsl.select(
               DSL.field("username", String.class).as("username"),
               DSL.field("email", String.class).as("email")
       )
               .from(DSL.table("profile"))
               .fetchOneInto(UserProfile.class);
    }

    // User DB queries

    public void validateConnection() {
        dsl.fetch("SELECT 1");
    }

    public void close() {
        System.out.println("Closing DB");
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
