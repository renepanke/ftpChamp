package io.github.renepanke.session.commands;

import io.github.renepanke.fs.FileSystem;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class Dele implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(Dele.class);

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();
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

    }
}
