package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.session.Session;
import io.github.renepanke.ftpchamp.commands.replies.Reply;

import java.net.Socket;

public class UnknownCommand implements Command {
    @Override
    public void handle(String argument, Session session, Socket socket) {
        Reply.PermanentNegativeCompletion.send_502_CommandNotImplemented(session);
    }
}
