package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.exceptions.FTPServerException;
import io.github.renepanke.ftpchamp.session.Session;
import io.github.renepanke.ftpchamp.session.TransferMode;
import io.github.renepanke.ftpchamp.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.arg;
import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;

public class MODE implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(MODE.class);

    private final Command innerCommand;

    public MODE() {
        innerCommand = auth(arg((argument, session, socket) -> {
            try {
                TransferMode transferMode = TransferMode.parseFromString(argument);
                session.setTransferMode(transferMode);
                // Maybe also 504 Command not implemented for that parameter if it's decided to only support STREAM or S.
                Reply.PositiveCompletion.send_200_CommandOkay(session);
            } catch (FTPServerException e) {
                LOG.atError().setCause(e).log("Can't set transfer mode.");
                Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
            }
        }));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
