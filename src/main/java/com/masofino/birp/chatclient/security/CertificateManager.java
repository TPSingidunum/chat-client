package com.masofino.birp.chatclient.security;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;

import com.masofino.birp.chatclient.config.AppConfig;

public class CertificateManager {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void generateCertificate(String username) throws Exception {
        AppConfig config = AppConfig.getInstance();

        // Create directories if they don't exist
        File certFile = new File(config.getCertificatePath());
        File keyFile = new File(config.getPrivateKeyPath());

        createDirectoryIfNeeded(certFile.getParentFile());
        createDirectoryIfNeeded(keyFile.getParentFile());

        // Generate RSA key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048); // 2048 bits should be sufficient
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Certificate details
        String subjectDN = "CN=" + username + ", O=BIRP Chat, C=RS";
        X500Name subject = new X500Name(subjectDN);
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());

        // Certificate validity period (1 year)
        Date notBefore = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // 1 day in the past to avoid time sync issues
        Date notAfter = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000); // 1 year

        // Create certificate builder
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                subject, // Self-signed, so issuer = subject
                serialNumber,
                notBefore,
                notAfter,
                subject,
                keyPair.getPublic()
        );

        // Sign the certificate
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA")
                .setProvider("BC")
                .build(keyPair.getPrivate());

        X509CertificateHolder certHolder = certBuilder.build(signer);
        X509Certificate certificate = new JcaX509CertificateConverter()
                .setProvider("BC")
                .getCertificate(certHolder);

        // Save certificate to file
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(certFile))) {
            pemWriter.writeObject(certificate);
        }

        // Save private key to file
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(keyFile))) {
            pemWriter.writeObject(keyPair.getPrivate());
        }
    }

    public static String getCertificateAsString() throws IOException {
        File certFile = new File(AppConfig.getInstance().getCertificatePath());
        if (!certFile.exists()) {
            throw new IOException("Certificate file not found: " + certFile.getAbsolutePath());
        }

        return Files.readString(certFile.toPath(), StandardCharsets.UTF_8);
    }

    private static void createDirectoryIfNeeded(File dir) {
        if (dir != null && !dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                System.err.println("Failed to create directory: " + dir.getAbsolutePath());
            }
        }
    }
}