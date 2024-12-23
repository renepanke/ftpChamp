package io.github.renepanke.ftpchamp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.renepanke.ftpchamp.exceptions.FTPServerRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

public class Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
    private static final AtomicReference<Configuration> instance = new AtomicReference<>();
    public static final int DEFAULT_SERVER_PORT = 21;
    public static final int DEFAULT_THREAD_POOL_SIZE = 10;
    public static final Path DEFAULT_WORKING_DIRECTORY = Path.of(System.getProperty("user.dir"));
    public static final int DEFAULT_PASSIVE_PORTS_LOWER_BOUND = 61000;
    public static final int DEFAULT_PASSIVE_PORTS_UPPER_BOUND = 61100;
    public static final long DEFAULT_FIND_PASSIVE_PORT_TIMEOUT_IN_MS = 5000L;
    private final int serverPort;
    private final int threadPoolSize;
    private final Path workingDirectory;
    private final int passiveRangePortsLowerBound;
    private final int passiveRangePortsUpperBound;
    private final long findPassivePortTimeoutInMs;
    private final Path certificatePath;
    private final Path privateKeyPath;
    private SSLContext sslContext;

    private Configuration() {
        try {
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            objectMapper.findAndRegisterModules();
            YamlConfiguration yamlConfiguration = objectMapper.readValue(Files.newInputStream(getConfigPath()), YamlConfiguration.class);
            this.serverPort = yamlConfiguration.getServerPort().orElse(DEFAULT_SERVER_PORT);
            this.threadPoolSize = yamlConfiguration.getThreadPoolSize().orElse(DEFAULT_THREAD_POOL_SIZE);
            this.workingDirectory = yamlConfiguration.getWorkingDirectory().map(Path::of).orElse(DEFAULT_WORKING_DIRECTORY);
            this.passiveRangePortsLowerBound = yamlConfiguration.getPassiveRangePortsLowerBound().orElse(DEFAULT_PASSIVE_PORTS_LOWER_BOUND);
            this.passiveRangePortsUpperBound = yamlConfiguration.getPassiveRangePortsUpperBound().orElse(DEFAULT_PASSIVE_PORTS_UPPER_BOUND);
            this.findPassivePortTimeoutInMs = yamlConfiguration.getFindPassivePortTimeoutInMs().orElse(DEFAULT_FIND_PASSIVE_PORT_TIMEOUT_IN_MS);
            this.certificatePath = yamlConfiguration.getCertificatePath().map(Path::of).orElse(null);
            this.privateKeyPath = yamlConfiguration.getPrivateKeyPath().map(Path::of).orElse(null);
            if (certificatePath != null && privateKeyPath != null) {
                sslContext = initializeSSLContext();
            }
        } catch (IOException e) {
            throw new FTPServerRuntimeException(e);
        }
    }

    private SSLContext initializeSSLContext() {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(new String(Files.readAllBytes(this.privateKeyPath))
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", ""));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(Files.newInputStream(certificatePath));

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setKeyEntry("alias", privateKey, null, new X509Certificate[]{certificate});

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, null);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            return sslContext;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | CertificateException |
                 KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
            LOG.error("", e);
            throw new FTPServerRuntimeException(e);
        }
    }

    public static Configuration get() {
        if (instance.get() == null) {
            instance.set(new Configuration());
        }
        return instance.get();
    }

    private static Path getConfigPath() {
        Path yamlPath;
        String configPathString = System.getenv("FTPCHAMP_CONFIG_FILE_PATH");
        if (configPathString != null) {
            yamlPath = Path.of(configPathString);
        } else {
            yamlPath = Path.of(System.getProperty("user.home"), ".config", "ftpchamp", "ftpchamp.yaml");
        }
        return yamlPath;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public Path getWorkingDirectory() {
        return workingDirectory;
    }

    public int getPassiveRangePortsLowerBound() {
        return passiveRangePortsLowerBound;
    }

    public int getPassiveRangePortsUpperBound() {
        return passiveRangePortsUpperBound;
    }

    public long getFindPassivePortTimeoutInMs() {
        return findPassivePortTimeoutInMs;
    }

    public Path getCertificatePath() {
        return certificatePath;
    }

    public Path getPrivateKeyPath() {
        return privateKeyPath;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }
}
