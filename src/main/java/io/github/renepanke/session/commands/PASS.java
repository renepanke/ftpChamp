package io.github.renepanke.session.commands;

import io.github.renepanke.session.Session;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.commands.replies.Reply;

public class PASS implements Command {

    /**
     * Valid status codes: 230, 202, 530, 500, 501, 503, 421, 332
     * @param argument
     * @param session
     */
    @Override
    public void handle(String argument, Session session) {

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
