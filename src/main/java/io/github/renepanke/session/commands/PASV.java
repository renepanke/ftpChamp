package io.github.renepanke.session.commands;

import io.github.renepanke.exceptions.FTPServerException;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;

public class PASV implements Command {

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();
        try {
            session.openPassiveSocket();
            Reply.PositiveCompletion.send_227_EnteringPassiveMode(session, session.getPassiveServerSocket().getInetAddress(), session.getPassiveDataPort());
            session.setConnectionMode(ConnectionMode.PASSIVE);
        } catch (FTPServerException e) {
            Reply.TransientNegativeCompletion.send_425_CantOpenDataConnection(session);
        }
    }
}
