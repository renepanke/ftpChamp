package io.github.renepanke.commands;

import io.github.renepanke.commands.shared.Command;
import io.github.renepanke.session.Session;

import java.net.Socket;

import static io.github.renepanke.commands.shared.Middleware.auth;

public class PWD implements Command {

    private final Command innerCommand;

    public PWD() {
        innerCommand = auth((argument, session, socket) -> {
            session.sendResponse("257 \"" + session.getWorkingDirectory().toAbsolutePath() + "\" is the current directory");
        });
    }

    /**
     * Valid status codes: 257,500,501,502,421,550
     *
     * @param argument
     * @param session
     */
    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }
}
