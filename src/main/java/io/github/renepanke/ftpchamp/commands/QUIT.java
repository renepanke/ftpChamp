package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.session.Session;
import io.github.renepanke.ftpchamp.commands.replies.Reply;

import java.net.Socket;

public class QUIT implements Command {

    @Override
    public void handle(String argument, Session session, Socket socket) {
        Reply.PositiveCompletion.send_221_ServiceClosingControlConnection(session);
        session.getSessionSpecificRequestHandler().quitSession();
    }
}
