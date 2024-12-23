package io.github.renepanke.commands;

import io.github.renepanke.commands.shared.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.CommandRegistry;

import java.net.Socket;

public class FEAT implements Command {

    @Override
    public void handle(String argument, Session session, Socket socket) {
        CommandRegistry.commands.keySet().stream()
                .map(feature -> "211- " + feature)
                .forEach(session::sendResponse);
        session.sendResponse("211 End");
    }
}
