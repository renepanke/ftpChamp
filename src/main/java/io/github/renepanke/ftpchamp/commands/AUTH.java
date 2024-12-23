package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.replies.Reply;
import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.exceptions.FTPServerException;
import io.github.renepanke.ftpchamp.session.SecurityMechanism;
import io.github.renepanke.ftpchamp.session.Session;

import java.net.Socket;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.arg;

public class AUTH implements Command {

    private final Command innerCommand;

    public AUTH() {
        innerCommand = arg((argument, session, socket) -> {
            if (session.getSecurityMechanism() != SecurityMechanism.UNINITIALIZED) {
                Reply.PermanentNegativeCompletion.send_503_BadSequenceOfCommands(session);
                return;
            }
            try {
                SecurityMechanism mechanism = SecurityMechanism.parseFromString(argument);
                switch (mechanism) {
                    case TLS, SSL -> {
                        Reply.PositiveCompletion.send_234_SecurityDataExchangeComplete(session);
                        session.upgradeConnectionToTls();
                    }
                    default ->
                            Reply.PermanentNegativeCompletion.send_504_CommandNotImplementedForThatParameter(session);
                }
            } catch (FTPServerException e) {
                session.setSecurityMechanism(SecurityMechanism.UNINITIALIZED);
                Reply.PermanentNegativeCompletion.send_504_CommandNotImplementedForThatParameter(session);
            }
        });
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
