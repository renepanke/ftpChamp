package io.github.renepanke.session.commands;

import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Syst implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Syst.class);

    @Override
    public void handle(String argument, Session session) {
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
