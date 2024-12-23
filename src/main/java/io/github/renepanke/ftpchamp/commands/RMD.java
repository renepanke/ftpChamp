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
import java.nio.file.Path;

import static io.github.renepanke.ftpchamp.lang.Bools.not;
import static io.github.renepanke.ftpchamp.commands.shared.Middleware.arg;
import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;

public class RMD implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(RMD.class);

    private final Command innerCommand;

    public RMD() {
        innerCommand = auth(arg((argument, session, socket) -> {
            try {
                Path pathOfDirToRemove = Path.of(argument);
                if (not(pathOfDirToRemove.isAbsolute())) {
                    pathOfDirToRemove = session.getWorkingDirectory().resolve(pathOfDirToRemove);
                }
                if (FileSystem.isNotADirectory(pathOfDirToRemove) || Files.notExists(pathOfDirToRemove)) {
                    Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
                    return;
                }
                Files.delete(pathOfDirToRemove);
                Reply.PositiveCompletion.send_250_RequestedFileActionOkayCompleted(session);
            } catch (IOException e) {
                LOG.atError().setCause(e).addArgument(argument).log("Could not delete directory <{}>");
                Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
            }
        }));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
