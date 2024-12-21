package io.github.renepanke.session.commands;

import io.github.renepanke.exceptions.FTPServerException;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class RETR implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(RETR.class);

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();

        try (Socket dataSocket = getConnectionModeMatchingSocket(session); OutputStream out = dataSocket.getOutputStream()) {
            Path pathToRetrieve = session.getWorkingDirectory().resolve(argument);
            Reply.PositivePreliminary.send_150_FileStatusOkayAboutToOpenDataConnection(session);
            try (InputStream in = Files.newInputStream(pathToRetrieve)) {
                byte[] byteBuffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(byteBuffer)) != -1) {
                    out.write(byteBuffer, 0, bytesRead);
                }
                Reply.PositiveCompletion.send_226_ClosingDataConnection(session);
            }
        } catch (IOException | FTPServerException e) {
            throw new RuntimeException(e);
        }
    }

    private Socket getConnectionModeMatchingSocket(Session session) throws FTPServerException {
        switch (session.getConnectionMode()) {
            case ACTIVE -> {
                try {
                    return new Socket(session.getActiveClientDataAddress(), session.getActiveClientDataPort());
                } catch (IOException e) {
                    throw new FTPServerException(e);
                }
            }
            case PASSIVE -> {
                try {
                    return session.acceptPassiveConnection();
                } catch (FTPServerException e) {
                    throw new FTPServerException(e);
                }
            }
            case EXTENDED_PASSIVE -> throw new FTPServerException("Extended passive mode not implemented for STOR");
            default -> throw new FTPServerException("Unknown connection mode " + session.getConnectionMode());
        }
    }
}
