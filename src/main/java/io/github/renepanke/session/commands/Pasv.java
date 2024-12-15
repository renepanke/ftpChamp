package io.github.renepanke.session.commands;

import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;

public class Pasv implements Command {

    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr530NotLoggedIn();

    }
}
