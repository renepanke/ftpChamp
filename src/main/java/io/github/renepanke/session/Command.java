package io.github.renepanke.session;

import java.net.Socket;

@FunctionalInterface
public interface Command {
    void handle(String argument, Session session, Socket socket);
}
