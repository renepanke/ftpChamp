package io.github.renepanke.session;

import io.github.renepanke.session.commands.*;

import java.util.Map;

public class CommandRegistry {

    public static final Map<String, Command> commands = Map.of(
            "CDUP", new CdUp(),
            "CWD", new Cwd(),
            "FEAT", new Feat(),
            "LIST", new List(),
            "PASS", new Pass(),
            "PORT", new Port(),
            "PWD", new Pwd(),
            "SYST", new Syst(),
            "TYPE", new Type(),
            "USER", new User()
    );

    public static Command get(String command) {
        return commands.getOrDefault(command, new UnknownCommand());
    }
}
