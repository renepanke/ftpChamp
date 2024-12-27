package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.replies.Reply;
import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.fs.FileSystem;
import io.github.renepanke.ftpchamp.session.Session;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.arg;
import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;

public class MDTM implements Command {

    private final Command innerCommand;

    public MDTM() {
        innerCommand = auth(arg(((argument, session, socket) -> {
            Path target = Path.of(argument);
            if (Files.notExists(target)) {
                Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
                return;
            }
            Reply.PositiveCompletion.send_213_FileStatus(FileSystem.modificationDateTime(target), session);
        })));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
