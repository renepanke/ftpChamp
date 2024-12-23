package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.session.DataTransferType;
import io.github.renepanke.ftpchamp.session.Session;
import io.github.renepanke.ftpchamp.commands.replies.Reply;

import java.net.Socket;
import java.util.Optional;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.arg;
import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;

public class TYPE implements Command {

    private final Command innerCommand;

    public TYPE() {
        innerCommand = auth(arg(((argument, session, socket) -> {
            Optional<DataTransferType> type = DataTransferType.fromTypeCodeLiteral(argument);
            if (type.isEmpty())
                Reply.PermanentNegativeCompletion.send_504_CommandNotImplementedForThatParameter(session);

            session.setDataTransferType(type.get());
            Reply.PositiveCompletion.send_200_CommandOkay(session);
        })));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}