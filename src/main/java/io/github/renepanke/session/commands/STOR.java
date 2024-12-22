package io.github.renepanke.session.commands;

import io.github.renepanke.exceptions.FTPServerException;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.SharedFileTransferFunctions;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class STOR implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(STOR.class);

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();

        try (Socket dataSocket = SharedFileTransferFunctions.getConnectionModeMatchingSocket(session); InputStream in = dataSocket.getInputStream()) {
            Path pathToStore = session.getWorkingDirectory().resolve(argument);
            Reply.PositivePreliminary.send_150_FileStatusOkayAboutToOpenDataConnection(session);
            try (OutputStream out = Files.newOutputStream(pathToStore)) {
                byte[] byteBuffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(byteBuffer)) != -1) {
                    out.write(byteBuffer, 0, bytesRead);
                }
                Reply.PositiveCompletion.send_226_ClosingDataConnection(session);
            }
        } catch (IOException | FTPServerException e) {
            LOG.atError().setCause(e).log("Failed to execute STOR command");
            Reply.TransientNegativeCompletion.send_425_CantOpenDataConnection(session);
        } finally {
            session.closePassiveSocket();
        }
    }
}
