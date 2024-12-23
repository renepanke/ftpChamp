package io.github.renepanke.commands;

import io.github.renepanke.commands.shared.Command;
import io.github.renepanke.fs.FileSystem;
import io.github.renepanke.session.Session;
import io.github.renepanke.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.renepanke.commands.shared.Middleware.arg;
import static io.github.renepanke.commands.shared.Middleware.auth;

public class SMNT implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(SMNT.class);
    private final Command innerCommand;

    public SMNT() {
        innerCommand = auth(arg(((argument, session, socket) -> {
            try {
                Path newPath = session.getWorkingDirectory().resolve(argument);

                if (Files.notExists(newPath) || FileSystem.isNotADirectory(newPath)) {
                    Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
                    return;
                }

                session.setWorkingDirectory(newPath);
                Reply.PositiveCompletion.send_250_RequestedFileActionOkayCompleted(session);
            } catch (Exception e) {
                LOG.atError().setCause(e).log("Failed to resolve path for SMNT");
                Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
            }
        })));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
