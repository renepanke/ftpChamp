package io.github.renepanke.session.commands;

import io.github.renepanke.exceptions.FTPServerRuntimeException;
import io.github.renepanke.fs.FileSystem;
import io.github.renepanke.lang.Strings;
import io.github.renepanke.session.Command;
import io.github.renepanke.session.Session;
import io.github.renepanke.session.commands.replies.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.renepanke.session.Middleware.auth;
import static io.github.renepanke.session.Middleware.data;

public class NLST implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(NLST.class);
    private static final boolean AUTO_FLUSH = true;

    private final Command command;

    public NLST() {
        command = auth(data(((argument, session, socket) -> {
            Path targetDir = session.getWorkingDirectory();
            if (Strings.isNotBlank(argument)) {
                targetDir = session.getWorkingDirectory().resolve(argument);
                if (Files.notExists(targetDir) || FileSystem.isNotADirectory(targetDir)) {
                    Reply.PermanentNegativeCompletion.send_550_RequestedActionNotTakenFileUnavailable(session);
                    return;
                }
            }
            try (Stream<Path> children = Files.list(targetDir); PrintWriter out = getPrintWriter(socket, session)) {
                String list = children.map(child -> child.getFileName().toString()).collect(Collectors.joining("\r\n"));
                LOG.atTrace().addArgument(() -> list).log("Sending the following list to data socket:\r\n{}");
                out.print(list);
            } catch (IOException e) {
                LOG.atError().setCause(e).addArgument(targetDir).log("Failed to list files in directory <{}>");
                throw new FTPServerRuntimeException(e);
            }
        })));
    }


    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.command.handle(argument, session, socket);
    }


    private static PrintWriter getPrintWriter(Socket dataSocket, Session session) {
        try {
            return new PrintWriter(new OutputStreamWriter(dataSocket.getOutputStream(), StandardCharsets.UTF_8), AUTO_FLUSH);
        } catch (IOException e) {
            LOG.atError().setCause(e).log("Failed to get writer for data socket.");
            Reply.TransientNegativeCompletion.send_425_CantOpenDataConnection(session);
            throw new FTPServerRuntimeException(e);
        }
    }
}
