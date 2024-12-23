package io.github.renepanke.session.commands;

import io.github.renepanke.session.Command;
import io.github.renepanke.session.FileRenameStatus;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static io.github.renepanke.session.Middleware.arg;
import static io.github.renepanke.session.Middleware.auth;

public class RNTO implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(RNTO.class);

    private final Command innerCommand;

    public RNTO() {
        innerCommand = auth(arg(((argument, session, socket) -> {
            if (session.getFileRenameStatus() == FileRenameStatus.UNINITIALIZED) {
                Reply.PermanentNegativeCompletion.send_503_BadSequenceOfCommands(session);
                return;
            }

            try {
                Path newPath = session.getWorkingDirectory().resolve(argument);

                if (Files.exists(newPath)) {
                    Reply.PermanentNegativeCompletion.send_553_RequestedActionNotTakenFileNameNotAllowed(session);
                    return;
                }
                Files.move(session.getFileRenameOldFile(), newPath, StandardCopyOption.ATOMIC_MOVE);
                Reply.PositiveCompletion.send_250_RequestedFileActionOkayCompleted(session);
            } catch (InvalidPathException e) {
                Reply.PermanentNegativeCompletion.send_553_RequestedActionNotTakenFileNameNotAllowed(session);
            } catch (IOException e) {
                LOG.atError().setCause(e).log("File rename to failed");
                Reply.PermanentNegativeCompletion.send_553_RequestedActionNotTakenFileNameNotAllowed(session);
            }
        })));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
