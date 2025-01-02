package io.github.renepanke.ftpchamp.session;

import io.github.renepanke.ftpchamp.RequestHandler;
import io.github.renepanke.ftpchamp.commands.replies.Reply;
import io.github.renepanke.ftpchamp.configuration.Configuration;
import io.github.renepanke.ftpchamp.exceptions.FTPServerException;
import io.github.renepanke.ftpchamp.exceptions.FTPServerRuntimeException;
import io.github.renepanke.ftpchamp.lang.Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Random;
import java.util.UUID;

import static io.github.renepanke.ftpchamp.lang.Bools.not;

public class Session {

    public static final int UNINITIALIZED_PORT = -1;
    public static final int DEFAULT_PASSIVE_BACKLOG = 50;
    public static final InetAddress DEFAULT_IP = InetAddress.getLoopbackAddress();

    private static final Logger LOG = LoggerFactory.getLogger(Session.class);
    public static final String CRLF = "\r\n";
    public static final int RANDOM_PORT = 0;
    private final String id;
    private BufferedReader in;
    private Socket socket;
    private PrintWriter out;
    private final RequestHandler sessionSpecificRequestHandler;
    private boolean authenticated = false;
    private Path workingDirectory = Configuration.get().getWorkingDirectory();
    private DataTransferType dataTransferType = DataTransferType.IMAGE;
    private InetAddress activeClientDataAddress;
    private int activeClientDataPort = UNINITIALIZED_PORT;
    private FileRenameStatus fileRenameStatus = FileRenameStatus.UNINITIALIZED;
    private Path fileRenameOldFile;
    private final FileStructure fileStructure = FileStructure.FILE;
    private TransferMode transferMode = TransferMode.STREAM;
    private ConnectionMode connectionMode = ConnectionMode.UNINITIALIZED;
    private ServerSocket passiveServerSocket;
    private int passiveDataPort = UNINITIALIZED_PORT;
    private SecurityMechanism securityMechanism = SecurityMechanism.UNINITIALIZED;
    private DataChannelProtectionLevel dataChannelProtectionLevel = DataChannelProtectionLevel.CLEAR;

    public Session(final Socket socket, final RequestHandler sessionRequestHandler) {
        this.id = Hash.getSHA1Length7For(UUID.randomUUID().toString());
        MDC.put("sessionId", id);
        this.socket = socket;
        this.sessionSpecificRequestHandler = sessionRequestHandler;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new FTPServerRuntimeException(e);
        }
    }

    public void sendResponse(String message) {
        this.out.print(message + CRLF);
        this.out.flush();
        LOG.atInfo().addArgument(message).log("Sending response <{}>");
    }

    public void requireAuthOr530NotLoggedIn() {
        if (this.isNotAuthenticated()) {
            Reply.PermanentNegativeCompletion.send_530_NotLoggedIn(this);
        }
    }

    public String getId() {
        return id;
    }

    public boolean isAuthenticated() {
        this.authenticated = true;
        return this.authenticated;
    }

    public boolean isNotAuthenticated() {
        return not(this.authenticated);
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Path getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public DataTransferType getDataTransferType() {
        return dataTransferType;
    }

    public void setDataTransferType(DataTransferType dataTransferType) {
        this.dataTransferType = dataTransferType;
    }

    public InetAddress getActiveClientDataAddress() {
        return activeClientDataAddress;
    }

    public void setActiveClientDataAddress(InetAddress activeClientDataAddress) {
        this.activeClientDataAddress = activeClientDataAddress;
    }

    public int getActiveClientDataPort() {
        return activeClientDataPort;
    }

    public void setActiveClientDataPort(int activeClientDataPort) {
        this.activeClientDataPort = activeClientDataPort;
    }

    public RequestHandler getSessionSpecificRequestHandler() {
        return sessionSpecificRequestHandler;
    }

    public Path getFileRenameOldFile() {
        return fileRenameOldFile;
    }

    public FileRenameStatus getFileRenameStatus() {
        return fileRenameStatus;
    }

    public FileStructure getFileStructure() {
        return fileStructure;
    }

    public TransferMode getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(TransferMode transferMode) {
        this.transferMode = transferMode;
    }

    public ConnectionMode getConnectionMode() {
        return connectionMode;
    }

    public void setConnectionMode(ConnectionMode connectionMode) {
        this.connectionMode = connectionMode;
    }

    public FileRenameStatus initializeFileRename(String oldFile) {
        try {
            this.fileRenameOldFile = workingDirectory.resolve(oldFile);
            this.fileRenameStatus = FileRenameStatus.INITIALIZED;
            return this.fileRenameStatus;
        } catch (InvalidPathException e) {
            this.fileRenameStatus = FileRenameStatus.FILE_NOT_FOUND;
            return this.fileRenameStatus;
        } catch (Exception e) {
            this.fileRenameStatus = FileRenameStatus.ERROR;
            LOG.atError().setCause(e).log("Failed to initialize file rename");
            throw new FTPServerRuntimeException(e);
        }
    }

    public void resetFileRenameStatus() {
        this.fileRenameOldFile = null;
        this.fileRenameStatus = FileRenameStatus.UNINITIALIZED;
    }

    public void openPassiveSocket() throws FTPServerException {
        if (passiveServerSocket != null && not(passiveServerSocket.isClosed())) {
            throw new FTPServerException("Passive server socket already open.");
        }
        LOG.atDebug().addArgument(this.dataChannelProtectionLevel).log("Opening passive socket with Data Channel Protection Level <{}>");
        if (DataChannelProtectionLevel.PRIVATE == dataChannelProtectionLevel) {
            try {
                SSLServerSocketFactory sslSocketFactory = Configuration.get().getSslContext().getServerSocketFactory();
                SSLServerSocket sslSocket = (SSLServerSocket) sslSocketFactory.createServerSocket(findAvailablePortInPortRange(), DEFAULT_PASSIVE_BACKLOG, DEFAULT_IP);
                this.passiveServerSocket = sslSocket;
                this.passiveDataPort = sslSocket.getLocalPort();
                sslSocket.setUseClientMode(false);
            } catch (IOException e) {
                LOG.atError().setCause(e).log("Failed to initialize security mechanism");
                Reply.PermanentNegativeCompletion.send_535_FailedSecurityCheck(this);
            }
        } else {
            try {
                passiveServerSocket = new ServerSocket(findAvailablePortInPortRange(), DEFAULT_PASSIVE_BACKLOG, DEFAULT_IP);
                passiveDataPort = passiveServerSocket.getLocalPort();
            } catch (IOException e) {
                throw new FTPServerException(e);
            }
        }
    }

    public Socket acceptPassiveConnection() throws FTPServerException {
        if (passiveServerSocket == null || passiveServerSocket.isClosed()) {
            throw new FTPServerException("No passive server socket open.");
        }
        try {
            return passiveServerSocket.accept();
        } catch (IOException e) {
            throw new FTPServerException(e);
        }
    }

    public void closePassiveSocket() {
        if (passiveServerSocket != null && not(passiveServerSocket.isClosed())) {
            try {
                Reply.PositiveCompletion.send_226_ClosingDataConnection(this);
                passiveServerSocket.close();
                LOG.atInfo().addArgument(() -> passiveServerSocket.getInetAddress().getHostAddress() + ":" + passiveServerSocket.getLocalPort())
                        .log("Closed PASV connection at <{}>");
            } catch (IOException e) {
                LOG.atWarn().setCause(e).log("Failed to close passive server socket");
            }
        }
        passiveServerSocket = null;
    }

    public int getPassiveDataPort() {
        return passiveDataPort;
    }

    public ServerSocket getPassiveServerSocket() {
        return passiveServerSocket;
    }

    private int findAvailablePortInPortRange() throws FTPServerException {
        long startTime = System.currentTimeMillis();
        Random random = new Random();
        long timeOutMs = Configuration.get().getFindPassivePortTimeoutInMs();
        Integer port = null;

        while (System.currentTimeMillis() - startTime < timeOutMs) {
            int currentRandomPort = Configuration.get().getPassiveRangePortsLowerBound() + random.nextInt(Configuration.get().getPassiveRangePortsUpperBound() - Configuration.get().getPassiveRangePortsLowerBound() + 1);
            if (isPortAvailable(currentRandomPort)) {
                port = currentRandomPort;
                break;
            }
        }
        if (port == null)
            throw new FTPServerException("Timeout reached while trying to find an available port in the specified port range.");

        return port;
    }

    private boolean isPortAvailable(int port) {
        try (ServerSocket s = new ServerSocket(port)) {
            s.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public SecurityMechanism getSecurityMechanism() {
        return securityMechanism;
    }

    public void setSecurityMechanism(SecurityMechanism securityMechanism) {
        switch (securityMechanism) {
            case TLS, SSL -> {
                this.securityMechanism = securityMechanism;
            }
            default -> {
                this.securityMechanism = SecurityMechanism.UNINITIALIZED;
            }
        }
    }

    public void upgradeConnectionToTls() {
        try {
            SSLSocketFactory sslSocketFactory = Configuration.get().getSslContext().getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(socket, socket.getInetAddress().getHostAddress(), socket.getLocalPort(), true);
            this.socket = sslSocket;
            this.out = new PrintWriter(sslSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            sslSocket.setUseClientMode(false);
            sslSocket.startHandshake();
            this.setSecurityMechanism(SecurityMechanism.TLS);
        } catch (IOException e) {
            LOG.atError().setCause(e).log("Failed to initialize security mechanism");
            Reply.PermanentNegativeCompletion.send_535_FailedSecurityCheck(this);
        }
    }

    public String readLine() throws IOException {
        return this.in.readLine();
    }

    public DataChannelProtectionLevel getDataChannelProtectionLevel() {
        return dataChannelProtectionLevel;
    }

    public void setDataChannelProtectionLevel(DataChannelProtectionLevel dataChannelProtectionLevel) {
        this.dataChannelProtectionLevel = dataChannelProtectionLevel;
    }
}
