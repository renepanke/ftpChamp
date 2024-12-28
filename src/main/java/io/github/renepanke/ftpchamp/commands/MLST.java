package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.replies.Reply;
import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.commands.shared.MachineListing;
import io.github.renepanke.ftpchamp.session.Session;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.*;

/**
 * Servers are not required to support any particular set of the available facts. However, servers SHOULD, if
 * conceivably possible, support at least the type, perm, size, unique, and modify facts.
 * MLST provides data about exactly the object named on its command line, and no others.
 */
public class MLST implements Command {

    private final Command innerCommand;

    public MLST() {
        innerCommand = auth(data(arg((argument, session, socket) -> {
            Path target = Path.of(argument);
            if (Files.notExists(target)) {
                Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
                return;
            }
            Reply.PositiveCompletion.send_250_RequestFileActionOkayCompletedMLST(MachineListing.compile(target, session) + "\r\n", session);
        })));
    }


    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
