package io.github.renepanke.session.commands;

import io.github.renepanke.fs.FileSystem;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class CWD implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(CWD.class);

    /**
     * Valid status codes: 250,500,501,502,421,530,550
     *
     * @param argument
     * @param session
     */
    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();
        if (argument == null) {
            LOG.error("No target directory provided in arguments, returning 501 Syntax error in parameters or arguments.");
            Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
        }

        try {
            Path newDir;
            if ("..".equals(argument)) {
                newDir = FileSystem.getParent(session.getWorkingDirectory());
            } else {
                newDir = session.getWorkingDirectory().resolve(argument);
            }
            if (FileSystem.isNotADirectory(newDir)) {
                LOG.atError().addArgument(argument)
                        .log("Given PATHNAME <{}> is not a directory, returning 450");
                Reply.TransientNegativeCompletion.send_450_RequestedFileActionNotTaken(session);
                return;
            }
            session.setWorkingDirectory(newDir);
            Reply.PositiveCompletion.send_250_RequestedFileActionOkayCompleted(session);
        } catch (InvalidPathException e) {
            Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
        }
    }
}
