package io.github.renepanke.commands;

import io.github.renepanke.commands.shared.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.commands.replies.Reply;

import java.net.Socket;

public class NOOP implements Command {

    @Override
    public void handle(String argument, Session session, Socket socket) {
        Reply.PositiveCompletion.send_200_CommandOkay(session);
    }
}
