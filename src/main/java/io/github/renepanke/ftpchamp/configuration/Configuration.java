package io.github.renepanke.ftpchamp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.renepanke.ftpchamp.exceptions.FTPServerRuntimeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class Configuration {

    private static final AtomicReference<Configuration> instance = new AtomicReference<>();
    public static final int DEFAULT_SERVER_PORT = 21;
    public static final int DEFAULT_THREAD_POOL_SIZE = 10;
    public static final Path DEFAULT_WORKING_DIRECTORY = Path.of(System.getProperty("user.dir"));
    public static final int DEFAULT_PASSIVE_PORTS_LOWER_BOUND = 61000;
    public static final int DEFAULT_PASSIVE_PORTS_UPPER_BOUND = 61100;
    private final int serverPort;
    private final int threadPoolSize;
    private final Path workingDirectory;
    private final int passiveRangePortsLowerBound;
    private final int passiveRangePortsUpperBound;

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
        } catch (IOException e) {
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
}
