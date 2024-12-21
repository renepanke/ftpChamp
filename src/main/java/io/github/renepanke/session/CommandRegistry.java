package io.github.renepanke.session;

import io.github.renepanke.session.commands.*;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    public static final Map<String, Command> commands = new HashMap<>() {{
        put("CDUP", new CDUP());
        put("CWD", new CWD());
        put("DELE", new DELE());
        put("FEAT", new FEAT());
        put("LIST", new LIST());
        put("PASS", new PASS());
        put("PORT", new PORT());
        put("PWD", new PWD());
        put("QUIT", new QUIT());
        put("SYST", new SYST());
        put("TYPE", new TYPE());
        put("USER", new USER());
        put("RNFR", new RNFR());
    }};

    public static Command get(String command) {
        return commands.getOrDefault(command, new UnknownCommand());
    }
}
