package io.github.renepanke.ftpchamp.configuration;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class YamlConfiguration {

    private Optional<Integer> serverPort = Optional.empty();
    private Optional<Integer> threadPoolSize = Optional.empty();
    private Optional<String> workingDirectory = Optional.empty();
    private Optional<Integer> passiveRangePortsLowerBound = Optional.empty();
    private Optional<Integer> passiveRangePortsUpperBound = Optional.empty();
    private Optional<Long> findPassivePortTimeoutInMs = Optional.empty();

    public YamlConfiguration() {
    }

    public YamlConfiguration(Optional<Integer> serverPort, Optional<Integer> threadPoolSize, Optional<String> workingDirectory, Optional<Integer> passiveRangePortsLowerBound, Optional<Integer> passiveRangePortsUpperBound, Optional<Long> passiveRangeFindPortTimeout) {
        this.serverPort = serverPort;
        this.threadPoolSize = threadPoolSize;
        this.workingDirectory = workingDirectory;
        this.passiveRangePortsLowerBound = passiveRangePortsLowerBound;
        this.passiveRangePortsUpperBound = passiveRangePortsUpperBound;
        this.findPassivePortTimeoutInMs = passiveRangeFindPortTimeout;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        YamlConfiguration that = (YamlConfiguration) o;
        return Objects.equals(serverPort, that.serverPort) && Objects.equals(threadPoolSize, that.threadPoolSize) && Objects.equals(workingDirectory, that.workingDirectory) && Objects.equals(passiveRangePortsLowerBound, that.passiveRangePortsLowerBound) && Objects.equals(passiveRangePortsUpperBound, that.passiveRangePortsUpperBound) && Objects.equals(findPassivePortTimeoutInMs, that.findPassivePortTimeoutInMs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverPort, threadPoolSize, workingDirectory, passiveRangePortsLowerBound, passiveRangePortsUpperBound, findPassivePortTimeoutInMs);
    }

    @Override
    public String toString() {
        return "YamlConfiguration{" +
                "port=" + serverPort +
                ", threadPoolSize=" + threadPoolSize +
                ", workingDirectory=" + workingDirectory +
                ", passiveRangePortsLowerBound=" + passiveRangePortsLowerBound +
                ", passiveRangePortsUpperBound=" + passiveRangePortsUpperBound +
                ", passiveRangeFindPortTimeout=" + findPassivePortTimeoutInMs +
                '}';
    }
}
