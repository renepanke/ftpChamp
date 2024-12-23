package io.github.renepanke.commands;

import io.github.renepanke.exceptions.FTPServerException;
import io.github.renepanke.session.Session;
import io.github.renepanke.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

import static io.github.renepanke.commands.Middleware.auth;

public class PASV implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(PASV.class);

    private final Command innerCommand;

    public PASV() {
        innerCommand = auth((argument, session, socket) -> {
            try {
                session.openPassiveSocket();
                LOG.atInfo().addArgument(() -> session.getPassiveServerSocket().getInetAddress().getHostAddress())
                        .addArgument(session::getPassiveDataPort)
                        .log("Opened PASV connection at <{}:{}>");
                Reply.PositiveCompletion.send_227_EnteringPassiveMode(session, session.getPassiveServerSocket().getInetAddress(), session.getPassiveDataPort());
                session.setConnectionMode(ConnectionMode.PASSIVE);
            } catch (FTPServerException e) {
                LOG.atError().setCause(e).log("Failed to open PASV session");
                Reply.TransientNegativeCompletion.send_425_CantOpenDataConnection(session);
            }
        });
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
