package io.github.renepanke.session.commands;

import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

public class SYST implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(SYST.class);

    @Override
    public void handle(String argument, Session session, Socket socket) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            session.sendResponse("215 DOS/360 system type.");
            return;
        }
        if (os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            session.sendResponse("215 UNIX system type.");
            return;
        }
        Reply.PermanentNegativeCompletion.send_502_CommandNotImplemented(session);
        LOG.atError().addArgument(() -> System.getProperty("os.name").toLowerCase())
                .log("Can't determine system type from <{}>");
    }
}
