package io.github.renepanke.session.commands;

import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;

import java.net.Socket;

public class USER implements Command {

    /**
     * USER &lt;SP&gt; &lt;username&gt; &lt;CRLF&gt;
     * <br>
     * <a href="https://datatracker.ietf.org/doc/html/rfc959#autoid-6">Valid status codes</a>:
     * 230, 331, 332, 421, 500, 501, 530
     *
     * @param argument
     * @param session
     */
    @Override
    public void handle(String argument, Session session, Socket socket) {
        if (session.isAuthenticated()) {
            session.sendResponse("230 User logged in, proceed.");
            return;
        }
        session.sendResponse("331 User name okay, need password.");
        // "332 Need account for login." is ignored here since it's used only in systems where access is linked to
        // specific accounts, such as billing systems or shared accounts.
    }
}
