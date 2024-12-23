package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.session.Session;
import io.github.renepanke.ftpchamp.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.*;

public class RETR implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(RETR.class);

    private final Command innerCommand;

    public RETR() {
        innerCommand = auth(data(arg((argument, session, socket) -> {
            try (OutputStream out = socket.getOutputStream()) {
                Path pathToRetrieve = session.getWorkingDirectory().resolve(argument);
                Reply.PositivePreliminary.send_150_FileStatusOkayAboutToOpenDataConnection(session);
                try (InputStream in = Files.newInputStream(pathToRetrieve)) {
                    byte[] byteBuffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(byteBuffer)) != -1) {
                        out.write(byteBuffer, 0, bytesRead);
                    }
                    Reply.PositiveCompletion.send_226_ClosingDataConnection(session);
                }
            } catch (IOException e) {
                LOG.atError().setCause(e).log("Failed to execute RETR command");
                Reply.TransientNegativeCompletion.send_425_CantOpenDataConnection(session);
            }
        })));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
