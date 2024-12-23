package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.replies.Reply;
import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.fs.FileSystem;
import io.github.renepanke.ftpchamp.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.nio.file.Path;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;

public class CDUP implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(CDUP.class);

    private final Command innerCommand;

    public CDUP() {
        innerCommand = auth((argument, session, ignored) -> {
            LOG.atDebug().addArgument(() -> session.getWorkingDirectory().toAbsolutePath().toString())
                    .log("Current working directory: <{}>");
            Path parent = FileSystem.getParent(session.getWorkingDirectory());
            if (parent == null) {
                Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
                return;
            }
            session.setWorkingDirectory(parent);
            Reply.PositiveCompletion.send_200_CommandOkay(session);
        });
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
