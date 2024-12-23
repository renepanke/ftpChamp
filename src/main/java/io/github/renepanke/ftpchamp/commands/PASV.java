package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.exceptions.FTPServerException;
import io.github.renepanke.ftpchamp.session.ConnectionMode;
import io.github.renepanke.ftpchamp.session.Session;
import io.github.renepanke.ftpchamp.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;

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
