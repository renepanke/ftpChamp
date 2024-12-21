package io.github.renepanke.session;

import io.github.renepanke.RequestHandler;
import io.github.renepanke.exceptions.FTPServerRuntimeException;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static io.github.renepanke.lang.Bools.not;

public class Session {

    public static final int UNINITIALIZED_ACTIVE_DATA_PORT = -1;

    private static final Logger LOG = LoggerFactory.getLogger(Session.class);
    public static final String CRLF = "\r\n";
    private final Socket socket;
    private final PrintWriter out;
    private final RequestHandler sessionSpecificRequestHandler;
    private boolean authenticated = false;
    private Path workingDirectory = Path.of(System.getProperty("user.home"));
    private DataTransferType dataTransferType = DataTransferType.IMAGE;
    private InetAddress dataAddress;
    private int dataPort = UNINITIALIZED_ACTIVE_DATA_PORT;
    private FileRenameStatus fileRenameStatus = FileRenameStatus.UNINITIALIZED;
    private Path fileRenameOldFile;

    public Session(final Socket socket, final RequestHandler sessionRequestHandler) {
        this.socket = socket;
        this.sessionSpecificRequestHandler = sessionRequestHandler;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
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

    public InetAddress getDataAddress() {
        return dataAddress;
    }

    public void setDataAddress(InetAddress dataAddress) {
        this.dataAddress = dataAddress;
    }

    public int getDataPort() {
        return dataPort;
    }

    public void setDataPort(int dataPort) {
        this.dataPort = dataPort;
    }

    public RequestHandler getSessionSpecificRequestHandler() {
        return sessionSpecificRequestHandler;
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
}
