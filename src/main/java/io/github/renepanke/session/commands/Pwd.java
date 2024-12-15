package io.github.renepanke.session.commands;

import io.github.renepanke.session.Session;
import io.github.renepanke.session.Command;

public class Pwd implements Command {

    /**
     * Valid status codes: 257,500,501,502,421,550
     * @param argument
     * @param session
     */
    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();
        session.sendResponse("257 \"" + session.getWorkingDirectory().toAbsolutePath() + "\" is the current directory");
    }
}
