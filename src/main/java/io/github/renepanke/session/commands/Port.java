package io.github.renepanke.session.commands;

import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Port implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Port.class);

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr503NotLoggedIn();
        try {
            String[] parts = argument.split(",");
            if (parts.length != 6) {
                Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
            }
            String ip = String.join(".", parts[0], parts[1], parts[2], parts[3]);
            int port = (Integer.parseInt(parts[4]) << 8) + Integer.parseInt(parts[5]);
            session.setDataAddress(InetAddress.getByName(ip));
            session.setDataPort(port);
            Reply.PositiveCompletion.send_200_CommandOkay(session);
        } catch (UnknownHostException e) {
            LOG.atError().setCause(e).addArgument(argument).log("Unable to resolve IP address from <{}>");
            Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
        }
    }
}
