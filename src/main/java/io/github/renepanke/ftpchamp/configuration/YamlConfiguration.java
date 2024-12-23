package io.github.renepanke.ftpchamp.configuration;

import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class YamlConfiguration {

    private Optional<Integer> serverPort;
    private Optional<Integer> threadPoolSize;
    private Optional<String> workingDirectory;
    private Optional<Integer> passiveRangePortsLowerBound;
    private Optional<Integer> passiveRangePortsUpperBound;

    public YamlConfiguration() {
    }

    public YamlConfiguration(Optional<Integer> serverPort, Optional<Integer> threadPoolSize, Optional<String> workingDirectory, Optional<Integer> passiveRangePortsLowerBound, Optional<Integer> passiveRangePortsUpperBound) {
        this.serverPort = serverPort;
        this.threadPoolSize = threadPoolSize;
        this.workingDirectory = workingDirectory;
        this.passiveRangePortsLowerBound = passiveRangePortsLowerBound;
        this.passiveRangePortsUpperBound = passiveRangePortsUpperBound;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        YamlConfiguration that = (YamlConfiguration) o;
        return Objects.equals(serverPort, that.serverPort) && Objects.equals(threadPoolSize, that.threadPoolSize) && Objects.equals(workingDirectory, that.workingDirectory) && Objects.equals(passiveRangePortsLowerBound, that.passiveRangePortsLowerBound) && Objects.equals(passiveRangePortsUpperBound, that.passiveRangePortsUpperBound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverPort, threadPoolSize, workingDirectory, passiveRangePortsLowerBound, passiveRangePortsUpperBound);
    }

    @Override
    public String toString() {
        return "YamlConfiguration{" +
                "port=" + serverPort +
                ", threadPoolSize=" + threadPoolSize +
                ", workingDirectory=" + workingDirectory +
                ", passiveRangePortsLowerBound=" + passiveRangePortsLowerBound +
                ", passiveRangePortsUpperBound=" + passiveRangePortsUpperBound +
                '}';
    }
}
