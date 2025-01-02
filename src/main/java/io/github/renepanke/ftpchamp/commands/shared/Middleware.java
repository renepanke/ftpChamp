package io.github.renepanke.ftpchamp.commands.shared;

import io.github.renepanke.ftpchamp.commands.replies.Reply;
import io.github.renepanke.ftpchamp.exceptions.FTPServerException;
import io.github.renepanke.ftpchamp.exceptions.FTPServerRuntimeException;
import io.github.renepanke.ftpchamp.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class Middleware {

    private static final Logger LOG = LoggerFactory.getLogger(Middleware.class);

    public static Command auth(Command command) {
        return (argument, session, socket) -> {
            if (session.isNotAuthenticated()) {
                LOG.error("Session is not authenticated - returning 530");
                Reply.PermanentNegativeCompletion.send_530_NotLoggedIn(session);
                return;
            }
            command.handle(argument, session, socket);
        };
    }

    public static Command arg(Command command) {
        return (argument, session, socket) -> {
            if (Strings.isBlank(argument)) {
                LOG.error("Argument is empty - returning 501");
                Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
                return;
            }
            command.handle(argument, session, socket);
        };
    }

    public static Command data(Command command) {
        return (argument, session, ignored) -> {
            try (Socket socket = FileTransferFunctions.getConnectionModeMatchingSocket(session)) {
                command.handle(argument, session, socket);
            } catch (IOException | FTPServerException e) {
                LOG.error("", e);
                throw new FTPServerRuntimeException(e);
            } finally {
                session.closePassiveSocket();
            }
        };
    }
}
