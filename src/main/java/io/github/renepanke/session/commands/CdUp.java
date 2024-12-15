package io.github.renepanke.session.commands;

import io.github.renepanke.fs.FileSystem;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class CdUp implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(CdUp.class);

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr503NotLoggedIn();
        LOG.atDebug().addArgument(() -> session.getWorkingDirectory().toAbsolutePath().toString())
                .log("Current working directory: <{}>");
        Path parent = FileSystem.getParent(session.getWorkingDirectory());
        if (parent == null) {
            Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
            return;
        }
        session.setWorkingDirectory(parent);
        Reply.PositiveCompletion.send_200_CommandOkay(session);
    }
}
