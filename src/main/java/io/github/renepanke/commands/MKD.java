package io.github.renepanke.commands;

import io.github.renepanke.session.Session;
import io.github.renepanke.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.renepanke.commands.Middleware.arg;
import static io.github.renepanke.commands.Middleware.auth;

public class MKD implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(MKD.class);

    private final Command innerCommand;

    public MKD() {
        innerCommand = auth(arg((argument, session, socket) -> {
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
        }));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
