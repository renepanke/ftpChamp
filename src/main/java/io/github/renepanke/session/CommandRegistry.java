package io.github.renepanke.session;

import io.github.renepanke.session.commands.*;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    public static final Map<String, Command> commands = new HashMap<>() {{
        put("CDUP", new CdUp());
        put("CWD", new Cwd());
        put("DELE", new Dele());
        put("FEAT", new Feat());
        put("LIST", new List());
        put("PASS", new Pass());
        put("PORT", new Port());
        put("PWD", new Pwd());
        put("QUIT", new Quit());
        put("SYST", new Syst());
        put("TYPE", new Type());
        put("USER", new User());
    }};

    public static Command get(String command) {
        return commands.getOrDefault(command, new UnknownCommand());
    }
}
