package io.github.renepanke.session.commands;

import io.github.renepanke.exceptions.FTPServerException;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PASV implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(PASV.class);

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();
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
    }
}
