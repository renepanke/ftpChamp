package io.github.renepanke.commands;

import io.github.renepanke.commands.shared.Command;
import io.github.renepanke.session.ConnectionMode;
import io.github.renepanke.session.Session;
import io.github.renepanke.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static io.github.renepanke.commands.shared.Middleware.arg;
import static io.github.renepanke.commands.shared.Middleware.auth;

public class PORT implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(PORT.class);

    private final Command innerCommand;

    public PORT() {
        innerCommand = auth(arg(((argument, session, socket) -> {
            try {
                String[] parts = argument.split(",");
                if (parts.length != 6) {
                    Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
                }
                String ip = String.join(".", parts[0], parts[1], parts[2], parts[3]);
                int port = (Integer.parseInt(parts[4]) << 8) + Integer.parseInt(parts[5]);
                session.setActiveClientDataAddress(InetAddress.getByName(ip));
                session.setActiveClientDataPort(port);
                session.setConnectionMode(ConnectionMode.ACTIVE);
                Reply.PositiveCompletion.send_200_CommandOkay(session);
            } catch (UnknownHostException e) {
                LOG.atError().setCause(e).addArgument(argument).log("Unable to resolve IP address from <{}>");
                Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
            }
        })));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
