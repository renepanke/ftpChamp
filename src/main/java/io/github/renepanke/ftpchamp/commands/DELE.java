package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.fs.FileSystem;
import io.github.renepanke.ftpchamp.session.Session;
import io.github.renepanke.ftpchamp.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.arg;
import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;

public class DELE implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(DELE.class);

    private final Command innerCommand;

    public DELE() {
        innerCommand = auth(arg((argument, session, socket) -> {
            try {
                Path fileToDelete = session.getWorkingDirectory().resolve(argument);
                if (FileSystem.isDirectory(fileToDelete)) {
                    Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
                }
                Files.delete(fileToDelete);
                Reply.PositiveCompletion.send_250_RequestedFileActionOkayCompleted(session);
            } catch (InvalidPathException | SecurityException | IOException e) {
                LOG.atError().setCause(e).addArgument(argument)
                        .log("Could not delete file: <{}>");
                Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
            }
        }));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
