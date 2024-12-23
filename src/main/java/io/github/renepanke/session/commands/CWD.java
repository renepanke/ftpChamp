package io.github.renepanke.session.commands;

import io.github.renepanke.fs.FileSystem;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static io.github.renepanke.session.Middleware.arg;
import static io.github.renepanke.session.Middleware.auth;

public class CWD implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(CWD.class);

    private final Command innerCommand;

    public CWD() {
        innerCommand = auth(arg((argument, session, ignored) -> {
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
        }));
    }

    /**
     * Valid status codes: 250,500,501,502,421,530,550
     *
     * @param argument
     * @param session
     */
    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
