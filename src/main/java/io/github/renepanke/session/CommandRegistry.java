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
        put("MODE", new MODE());
        put("PASS", new PASS());
        put("PORT", new PORT());
        put("PWD", new PWD());
        put("QUIT", new QUIT());
        put("RNFR", new RNFR());
        put("RNTO", new RNTO());
        put("SMNT", new SMNT());
        put("STOR", new STOR());
        put("SYST", new SYST());
        put("TYPE", new TYPE());
        put("USER", new USER());
    }};

    public static Command get(String command) {
        return commands.getOrDefault(command, new UnknownCommand());
    }
}
