package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.replies.Reply;
import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.exceptions.FTPServerException;
import io.github.renepanke.ftpchamp.session.DataChannelProtectionLevel;
import io.github.renepanke.ftpchamp.session.Session;

import java.net.Socket;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.arg;
import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;

public class PROT implements Command {

    private final Command innerCommand;

    public PROT() {
        innerCommand = auth(arg(((argument, session, socket) -> {
            try {
                session.setDataChannelProtectionLevel(DataChannelProtectionLevel.parseFromString(argument));
                Reply.PositiveCompletion.send_200_CommandOkay(session);
            } catch (FTPServerException e) {
                Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
            }
        })));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
