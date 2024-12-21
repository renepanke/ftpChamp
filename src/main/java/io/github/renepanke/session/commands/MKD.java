package io.github.renepanke.session.commands;

import io.github.renepanke.lang.Strings;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MKD implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(MKD.class);

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();

        if (Strings.isBlank(argument)) {
            Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
            return;
        }

        try {
            Path newDirPath = Path.of(argument);
            if (newDirPath.isAbsolute()) {
                Files.createDirectory(newDirPath);
            } else {
                newDirPath = session.getWorkingDirectory().resolve(newDirPath);
                Files.createDirectory(newDirPath);
            }
            Reply.PositiveCompletion.send_257_PathnameCreated(argument, session);
        } catch (IOException e) {
            LOG.atError().setCause(e).addArgument(argument).log("Could not create directory <{}>");
            Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
        }
    }
}
