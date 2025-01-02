package io.github.renepanke.ftpchamp.commands.shared;

import io.github.renepanke.ftpchamp.exceptions.FTPServerException;
import io.github.renepanke.ftpchamp.session.Session;

import java.io.IOException;
import java.net.Socket;

public final class FileTransferFunctions {

    private FileTransferFunctions() {
        throw new AssertionError();
    }

    public static Socket getConnectionModeMatchingSocket(Session session) throws FTPServerException {
        switch (session.getConnectionMode()) {
            case ACTIVE -> {
                try {
                    return new Socket(session.getActiveClientDataAddress(), session.getActiveClientDataPort());
                } catch (IOException e) {
                    throw new FTPServerException(e);
                }
            }
            case EXTENDED_PASSIVE, PASSIVE -> {
                try {
                    return session.acceptPassiveConnection();
                } catch (FTPServerException e) {
                    throw new FTPServerException(e);
                }
            }
            default -> throw new FTPServerException("Unknown connection mode " + session.getConnectionMode());
        }
    }
}
