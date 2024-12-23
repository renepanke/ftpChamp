package io.github.renepanke.ftpchamp.commands.shared;

import io.github.renepanke.ftpchamp.session.Session;

import java.net.Socket;

@FunctionalInterface
public interface Command {
    void handle(String argument, Session session, Socket socket);
}
