package io.github.renepanke.session;

public interface Command {

    void handle(String argument, Session session);

}
