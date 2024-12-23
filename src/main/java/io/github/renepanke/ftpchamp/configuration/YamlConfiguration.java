package io.github.renepanke.ftpchamp.configuration;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class YamlConfiguration {

    private Optional<Integer> serverPort = Optional.empty();
    private Optional<Integer> threadPoolSize = Optional.empty();
    private Optional<String> workingDirectory = Optional.empty();
    private Optional<Integer> passiveRangePortsLowerBound = Optional.empty();
    private Optional<Integer> passiveRangePortsUpperBound = Optional.empty();
    private Optional<Long> findPassivePortTimeoutInMs = Optional.empty();
    private Optional<String> certificatePath = Optional.empty();
    private Optional<String> privateKeyPath = Optional.empty();

    public YamlConfiguration() {
    }

    public YamlConfiguration(Optional<Integer> serverPort, Optional<Integer> threadPoolSize, Optional<String> workingDirectory, Optional<Integer> passiveRangePortsLowerBound, Optional<Integer> passiveRangePortsUpperBound, Optional<Long> passiveRangeFindPortTimeout, Optional<String> certificatePath, Optional<String> privateKeyPath) {
        this.serverPort = serverPort;
        this.threadPoolSize = threadPoolSize;
        this.workingDirectory = workingDirectory;
        this.passiveRangePortsLowerBound = passiveRangePortsLowerBound;
        this.passiveRangePortsUpperBound = passiveRangePortsUpperBound;
        this.findPassivePortTimeoutInMs = passiveRangeFindPortTimeout;
        this.certificatePath = certificatePath;
        this.privateKeyPath = privateKeyPath;
    }

    public Optional<Integer> getServerPort() {
        return serverPort;
    }

    public void setServerPort(Optional<Integer> serverPort) {
        this.serverPort = serverPort;
    }

    public Optional<Integer> getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(Optional<Integer> threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public Optional<String> getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(Optional<String> workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public Optional<Integer> getPassiveRangePortsLowerBound() {
        return passiveRangePortsLowerBound;
    }

    public void setPassiveRangePortsLowerBound(Optional<Integer> passiveRangePortsLowerBound) {
        this.passiveRangePortsLowerBound = passiveRangePortsLowerBound;
    }

    public Optional<Integer> getPassiveRangePortsUpperBound() {
        return passiveRangePortsUpperBound;
    }

    public void setPassiveRangePortsUpperBound(Optional<Integer> passiveRangePortsUpperBound) {
        this.passiveRangePortsUpperBound = passiveRangePortsUpperBound;
    }

    public Optional<Long> getFindPassivePortTimeoutInMs() {
        return findPassivePortTimeoutInMs;
    }

    public void setFindPassivePortTimeoutInMs(Optional<Long> findPassivePortTimeoutInMs) {
        this.findPassivePortTimeoutInMs = findPassivePortTimeoutInMs;
    }

    public Optional<String> getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(Optional<String> certificatePath) {
        this.certificatePath = certificatePath;
    }

    public Optional<String> getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(Optional<String> privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }
}
