package io.github.renepanke.session.commands;

import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;

public class NOOP implements Command {

    @Override
    public void handle(String argument, Session session) {
        Reply.PositiveCompletion.send_200_CommandOkay(session);
    }
}
