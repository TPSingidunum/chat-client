package ac.rs.singidunum.chatclient.services;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CertificateService {

    private String commonName = "Singidunum";
    private String orgUnit = "BiRP";
    private String org;
    private String locality = "RS";
    private String state = "RS";
    private String country = "RS";
    public static int keySize = 4096;
    private int caValidDuration = 3650;
    public static String userCertName = "user.cert";

    public void setOrg(String org) {
        this.org = org;
    }

    private String toX500Principal() {
        return String.format("CN=%s, OU=%s, O=%s, L=%s, ST=%s, C=%s",commonName, orgUnit, org, locality, state, country);
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public X509Certificate generateCACertificate(KeyPair kp) throws NoSuchAlgorithmException, CertIOException, OperatorCreationException, CertificateException {
        // Parametri za sertifikat
        X500Name subject = new X500Name(this.toX500Principal());
        BigInteger serial = generateSerialNumber();
        Date notBefore = Date.from(Instant.now());
        Date notAfter = Date.from(Instant.now().plus(this.caValidDuration, ChronoUnit.DAYS));

        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
            subject, serial, notBefore, notAfter, subject, kp.getPublic()
        );

        // Dodavanje privilegija sertifikatu
        JcaX509ExtensionUtils extUtils = new JcaX509ExtensionUtils();

        builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(1));
        builder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
        builder.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(kp.getPublic()));

        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider("BC")
                .build((kp.getPrivate()));

        return new JcaX509CertificateConverter()
                .setProvider("BC")
                .getCertificate(builder.build(signer));
    }

    public static KeyPair generateRSAKeys(int keyLength) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(keyLength, new SecureRandom());
        return kpg.generateKeyPair();
    }

    private BigInteger generateSerialNumber() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return new BigInteger(bytes).abs();
    }

    public void writePem(Path target, Object object) {
        try(JcaPEMWriter pw = new JcaPEMWriter(new FileWriter(target.toFile()))) {
            pw.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
