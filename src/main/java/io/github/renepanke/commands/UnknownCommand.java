package io.github.renepanke.commands;

import io.github.renepanke.session.Session;
import io.github.renepanke.commands.replies.Reply;

import java.net.Socket;

public class UnknownCommand implements Command {
    @Override
    public void handle(String argument, Session session, Socket socket) {
        Reply.PermanentNegativeCompletion.send_502_CommandNotImplemented(session);
    }
}
