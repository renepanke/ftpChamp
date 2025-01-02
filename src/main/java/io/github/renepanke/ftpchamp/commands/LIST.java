package io.github.renepanke.ftpchamp.commands;

import io.github.renepanke.ftpchamp.commands.replies.Reply;
import io.github.renepanke.ftpchamp.commands.shared.Command;
import io.github.renepanke.ftpchamp.exceptions.FTPServerRuntimeException;
import io.github.renepanke.ftpchamp.fs.FileSystem;
import io.github.renepanke.ftpchamp.lang.Strings;
import io.github.renepanke.ftpchamp.session.Session;
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

import static io.github.renepanke.ftpchamp.commands.shared.Middleware.auth;
import static io.github.renepanke.ftpchamp.commands.shared.Middleware.data;
import static io.github.renepanke.ftpchamp.lang.Bools.not;

public class LIST implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(LIST.class);
    private static final boolean AUTO_FLUSH = true;

    private final Command innerCommand;

    public LIST() {
        innerCommand = auth(data((argument, session, socket) -> {
            switch (session.getConnectionMode()) {
                case ACTIVE -> {
                    if (session.getActiveClientDataAddress() == null || session.getActiveClientDataPort() == Session.UNINITIALIZED_PORT) {
                        LOG.warn("Need to call PORT before calling list when using an active connection, returning 503 Bad Sequence of Commands.");
                        Reply.PermanentNegativeCompletion.send_503_BadSequenceOfCommands(session);
                        return;
                    }
                }
                case PASSIVE -> {
                    if (session.getPassiveServerSocket() == null || session.getPassiveDataPort() == Session.UNINITIALIZED_PORT) {
                        LOG.warn("Need to call PASV before calling list when using a passive connection, returning 503 Bad Sequence of Commands.");
                        Reply.PermanentNegativeCompletion.send_503_BadSequenceOfCommands(session);
                        return;
                    }
                }
                case UNINITIALIZED -> {
                    LOG.warn("Neither PASV nor PORT was called before retrieving list, returning 503 Bad Sequence of Commands.");
                    Reply.PermanentNegativeCompletion.send_503_BadSequenceOfCommands(session);
                    return;
                }
                default -> {
                    Reply.PermanentNegativeCompletion.send_502_CommandNotImplemented(session);
                    return;
                }
            }

            Reply.PositivePreliminary.send_150_FileStatusOkayAboutToOpenDataConnection(session);
            switch (session.getConnectionMode()) {
                case ACTIVE, PASSIVE -> sendList(argument, session, socket);
                case EXTENDED_PASSIVE -> Reply.PermanentNegativeCompletion.send_502_CommandNotImplemented(session);
                default -> Reply.PermanentNegativeCompletion.send_503_BadSequenceOfCommands(session);
            }
        }));
    }

    /**
     * LIST [<SP> <pathname>] <CRLF>
     * <br>
     * Valid status codes: 125,150,226,250,421,425,426,450,451,500,501,502,530
     *
     * @param argument
     * @param session
     */
    @Override
    public void handle(String argument, Session session, Socket socket) {
        this.innerCommand.handle(argument, session, socket);
    }

    private void sendList(String argument, Session session, Socket socket) {
        LOG.trace("LIST.sendList()");
//        try (PrintWriter out = getPrintWriter(socket, session)) {
//            sendListToPrintWriter(argument, session, out);
//            Reply.PositiveCompletion.send_226_ClosingDataConnection(session);
//        }
        sendListToPrintWriter(argument, session, getPrintWriter(socket, session));
    }

    private void sendListToPrintWriter(String argument, Session session, PrintWriter out) {
        LOG.trace("LIST.sendListToPrintWriter()");
        String list = generateList(argument, session);
        LOG.atTrace().addArgument(() -> list).log("Sending the following list to data socket:\r\n{}");
        out.print(list);
    }

    private String generateList(String argument, Session session) {
        Path targetDir = session.getWorkingDirectory();
        if (Strings.isNotBlank(argument)) {
            targetDir = targetDir.resolve(argument);
        }

        if (Files.notExists(targetDir) || not(Files.isDirectory(targetDir))) {
            LOG.atError().addArgument(argument).log("Target directory <{}> does not exist or is not a directory");
            return "450 Requested file action not taken.";
        }
        try (Stream<Path> children = Files.list(targetDir)) {
            return children.map(this::generateListEntry).collect(Collectors.joining("\r\n"));
        } catch (IOException e) {
            LOG.atError().setCause(e).addArgument(targetDir).log("Failed to list files in directory <{}>");
            throw new FTPServerRuntimeException(e);
        }
    }

    private String generateListEntry(Path path) {
        LOG.trace("Entering generateListEntry");
        return String.format("%s %2d %-8s %-8s %8d %s %s",
                FileSystem.unixLikePermissionString(path), FileSystem.linkCount(path), FileSystem.owner(path), FileSystem.group(path), FileSystem.size(path), FileSystem.modificationDate(path), FileSystem.name(path));
    }

    private static PrintWriter getPrintWriter(Socket dataSocket, Session session) {
        LOG.atTrace().addArgument(dataSocket.getInetAddress().getHostName())
                .addArgument(dataSocket.getPort())
                .addArgument(session.getId())
                .log("LIST.getPrintWrite({},{}:{})");
        try {
            return new PrintWriter(new OutputStreamWriter(dataSocket.getOutputStream(), StandardCharsets.UTF_8), AUTO_FLUSH);
        } catch (IOException e) {
            LOG.atError().setCause(e).log("Failed to get writer for data socket.");
            Reply.TransientNegativeCompletion.send_425_CantOpenDataConnection(session);
            throw new FTPServerRuntimeException(e);
        }
    }

}
