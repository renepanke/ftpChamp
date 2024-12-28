package io.github.renepanke.ftpchamp.session;

import io.github.renepanke.ftpchamp.commands.*;
import io.github.renepanke.ftpchamp.commands.shared.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    public static final Map<String, Command> commands = new HashMap<>() {{
        put("AUTH", new AUTH());
        put("CDUP", new CDUP());
        put("CWD", new CWD());
        put("DELE", new DELE());
        put("FEAT", new FEAT());
        put("LIST", new LIST());
        put("MDTM", new MDTM());
        put("MKD", new MKD());
        put("MLST", new MLST());
        put("MODE", new MODE());
        put("NOOP", new NOOP());
        put("PASS", new PASS());
        put("PASV", new PASV());
        put("PORT", new PORT());
        put("PWD", new PWD());
        put("QUIT", new QUIT());
        put("RETR", new RETR());
        put("RMD", new RMD());
        put("RNFR", new RNFR());
        put("RNTO", new RNTO());
        put("SIZE", new SIZE());
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
