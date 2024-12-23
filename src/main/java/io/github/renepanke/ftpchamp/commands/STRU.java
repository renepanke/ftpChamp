package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.session.FileStructure;
import io.github.renepanke.ftpchamp.session.Session;
import io.github.renepanke.ftpchamp.commands.replies.Reply;

import java.net.Socket;

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.arg;
import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;

public class STRU implements Command {

    private final Command innerCommand;

    public STRU() {
        innerCommand = auth(arg(((argument, session, socket) -> {
            FileStructure fileStructure = FileStructure.parseFromString(argument);
            switch (fileStructure) {
                case FILE -> Reply.PositiveCompletion.send_200_CommandOkay(session);
                case RECORD, PAGE -> Reply.PermanentNegativeCompletion.send_502_CommandNotImplemented(session);
                case UNKNOWN -> Reply.PermanentNegativeCompletion.send_501_SyntaxErrorInParametersOrArguments(session);
            }
        })));
    }

    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
