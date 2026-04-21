package ac.rs.singidunum.chatclient.controllers;

import ac.rs.singidunum.chatclient.configs.AppConfig;
import ac.rs.singidunum.chatclient.configs.DbConfig;
import ac.rs.singidunum.chatclient.services.CertificateService;
import ac.rs.singidunum.chatclient.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.operator.OperatorCreationException;

import java.io.File;
import java.nio.file.Path;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class RegisterController {
    public TextField usernameField;
    public TextField emailField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;
    public Label messageLabel;

    public void onRegisterClick(ActionEvent actionEvent) {
        // Validation
        if (!validate()) {
            return;
        }

        // Generisem User certificate
        CertificateService cs = new CertificateService();
        cs.setOrg(usernameField.getText());
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(CertificateService.keySize);
            X509Certificate cert = cs.generateCACertificate(kpg.generateKeyPair());

            // Pitamo korisnika gde on zeli da se sertifikat sacuva
            // U tokenu, ESP32 S3 ploca
            // U local storage

            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Choose Certificate save location");
            File dir = dc.showDialog(SceneManager.getPrimaryStage());
            Path userCert =  dir.toPath().resolve(CertificateService.userCertName);

            cs.writePem(userCert, cert);
            AppConfig.getInstance().setProperty("user.cert", userCert.toString());

        } catch (NoSuchAlgorithmException | CertIOException | OperatorCreationException | CertificateException e) {
            throw new RuntimeException(e);
        }

        // Generisem bazu
        FileChooser fc = new FileChooser();
        fc.setTitle("Chose DbFile Storage location");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLite DB", "*.db"));
        fc.setInitialFileName("chat-storage.db");
        File dbPath = fc.showSaveDialog(SceneManager.getPrimaryStage());

        DbConfig.getInstance().initializeDb(passwordField.getText(), dbPath.toPath());
        DbConfig.getInstance().createUserProfile(usernameField.getText(), emailField.getText());
        AppConfig.getInstance().setProperty("db.path", dbPath.getPath());

        // Onemogucim registraciju, i postavim da podrazumevano otravaram trenutnog korisnika
        SceneManager.getInstance().switchScene("login-view");
    }

    private boolean validate() {
        if (usernameField.getCharacters().length() < 3) {
            messageLabel.setText("Username has to be longer than 3 chars");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            messageLabel.setText("Confirm password is wrong");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        return true;
    }

    public void onClearClick(ActionEvent actionEvent) {
    }

    public void onBackClick(ActionEvent actionEvent) {
        SceneManager.getInstance().switchScene("login-view");
    }
}
