package io.github.renepanke.commands;

import io.github.renepanke.session.Session;

import java.net.Socket;

@FunctionalInterface
public interface Command {
    void handle(String argument, Session session, Socket socket);
}
