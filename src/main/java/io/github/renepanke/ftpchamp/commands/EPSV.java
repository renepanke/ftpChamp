package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.replies.Reply;
import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.exceptions.FTPServerException;
import io.github.renepanke.ftpchamp.session.ConnectionMode;
import io.github.renepanke.ftpchamp.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;

public class EPSV implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(EPSV.class);

    private final Command innerCommand;

    public EPSV() {
        this.innerCommand = auth((argument, session, socket) -> {
            try {
                session.openPassiveSocket();
                LOG.atInfo().addArgument(() -> session.getPassiveServerSocket().getInetAddress().getHostAddress())
                        .addArgument(session::getPassiveDataPort)
                        .log("Opened EPSV connection at <{}:{}>");
                Reply.PositiveCompletion.send_229_EnteringExtendedPassiveMode(session, session.getPassiveDataPort());
                session.setConnectionMode(ConnectionMode.PASSIVE);
            } catch (FTPServerException e) {
                LOG.atError().setCause(e).log("Failed to open EPSV session");
                Reply.TransientNegativeCompletion.send_425_CantOpenDataConnection(session);
            }
        });
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
