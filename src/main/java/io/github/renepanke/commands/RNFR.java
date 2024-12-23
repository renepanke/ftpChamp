package io.github.renepanke.commands;

import io.github.renepanke.commands.shared.Command;
import io.github.renepanke.session.FileRenameStatus;
import io.github.renepanke.session.Session;
import io.github.renepanke.commands.replies.Reply;

import java.net.Socket;

import static io.github.renepanke.commands.shared.Middleware.arg;
import static io.github.renepanke.commands.shared.Middleware.auth;

public class RNFR implements Command {

    private final Command innerCommand;

    public RNFR() {
        innerCommand = auth(arg(((argument, session, socket) -> {
            FileRenameStatus status = session.initializeFileRename(argument);
            switch (status) {
                case INITIALIZED ->
                        Reply.PositiveIntermediate.send_350_RequestedFileActionPendingFurtherInformation(session);
                case FILE_NOT_FOUND, ERROR, UNINITIALIZED ->
                        Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
            }
        })));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
