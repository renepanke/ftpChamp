package io.github.renepanke.session.commands;

import io.github.renepanke.fs.FileSystem;
import io.github.renepanke.lang.Strings;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class SMNT implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(SMNT.class);

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();

        if (argument == null || Strings.isBlank(argument)) {
            Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
            return;
        }

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
    }
}
