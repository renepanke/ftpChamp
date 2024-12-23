package io.github.renepanke.commands;

import io.github.renepanke.session.Session;
import io.github.renepanke.commands.replies.Reply;

import java.net.Socket;

public class PASS implements Command {

    /**
     * Valid status codes: 230, 202, 530, 500, 501, 503, 421, 332
     *
     * @param argument
     * @param session
     */
    @Override
    public void handle(String argument, Session session, Socket socket) {
        /*
        When authentication is implemented check here!
        if ("PASSWORD".equals(argument)) {
            session.sendResponse("230 User logged in, proceed.");
        }
        session.sendResponse("530 Not logged in.");
         */
        Reply.PositiveCompletion.send_202_CommandNotImplementedSuperflousAtThisSite(session);
    }
}
