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

import static io.github.renepanke.lang.Bools.not;

public class List implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(List.class);
    public static final boolean AUTO_FLUSH = true;

    /**
     * LIST [<SP> <pathname>] <CRLF>
     * <br>
     * Valid status codes: 125,150,226,250,421,425,426,450,451,500,501,502,530
     *
     * @param argument
     * @param session
     */
    @Override
    public void handle(String argument, Session session) {
        session.requireAuthOr503NotLoggedIn();

        if (session.getDataAddress() == null || session.getDataPort() == Session.UNINITIALIZED_ACTIVE_DATA_PORT) {
            LOG.debug("Need to call PASV or PORT before calling list, returning 503 Bad Sequence of Commands.");
            Reply.PermanentNegativeCompletion.send_503_BadSequenceOfCommands(session);
            return;
        }

        Reply.PositivePreliminary.send_150_FileStatusOkayAboutToOpenDataConnection(session);
        sendList(argument, session);
    }

    private void sendList(String argument, Session session) {
        Socket dataSocket = null;
        PrintWriter out = null;

        try {
            dataSocket = getDataSocket(session);
            out = getPrintWriter(dataSocket, session);
            String list = generateList(argument, session);
            // FIXME:   Dies somewhere in generateListEntry
            LOG.atTrace().addArgument(() -> list).log("Sending the following list to data socket:\r\n{}");
            out.print(list);
        } catch (Exception e) {
            LOG.error("", e);
        }
        finally {
            Reply.PositiveCompletion.send_226_ClosingDataConnection(session);
            if (out != null) out.close();
            if (dataSocket != null && not(dataSocket.isClosed())) {
                try {
                    dataSocket.close();
                } catch (IOException e) {
                    LOG.atError().setCause(e).log("Failed to close data socket");
                }
            }
        }
    }

    private String generateList(String argument, Session session) {
        LOG.trace("Entering generateList");
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
        try {
            return new PrintWriter(new OutputStreamWriter(dataSocket.getOutputStream(), StandardCharsets.UTF_8), AUTO_FLUSH);
        } catch (IOException e) {
            LOG.atError().setCause(e).log("Failed to get writer for data socket.");
            Reply.TransientNegativeCompletion.send_425_CantOpenDataConnection(session);
            throw new FTPServerRuntimeException(e);
        }
    }

    private static Socket getDataSocket(Session session) {
        try {
            return new Socket(session.getDataAddress(), session.getDataPort());
        } catch (IOException | SecurityException e) {
            LOG.atError().setCause(e).log("Failed to create data socket");
            Reply.TransientNegativeCompletion.send_425_CantOpenDataConnection(session);
            throw new FTPServerRuntimeException(e);
        } catch (IllegalArgumentException e) {
            LOG.atError().setCause(e)
                    .addArgument(session::getDataPort)
                    .log("Failed to create data socket because specified port <{}> is invalid.");
            Reply.TransientNegativeCompletion.send_451_RequestedActionAbortedLocalErrorInProcessing(session);
            throw new FTPServerRuntimeException(e);
        } catch (NullPointerException e) {
            LOG.atError().setCause(e)
                    .log("Data Address from session is null.");
            throw new FTPServerRuntimeException(e);
        }
    }
}
