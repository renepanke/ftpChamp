package io.github.renepanke.session.commands;

import io.github.renepanke.session.Command;
import io.github.renepanke.session.DataTransferType;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;

import java.net.Socket;
import java.util.Optional;

import static io.github.renepanke.session.Middleware.arg;
import static io.github.renepanke.session.Middleware.auth;

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