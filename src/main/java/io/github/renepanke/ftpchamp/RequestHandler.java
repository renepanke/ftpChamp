package io.github.renepanke.ftpchamp;

import io.github.renepanke.ftpchamp.session.Session;
import io.github.renepanke.ftpchamp.session.CommandRegistry;
import io.github.renepanke.ftpchamp.commands.shared.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class RequestHandler implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket clientSocket;
    private final AtomicBoolean shallAcceptConnections = new AtomicBoolean(true);

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        try {
            Session context = new Session(this.clientSocket, this);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            context.sendResponse("220 Service ready for new user.");

            while (this.shallAcceptConnections.get()) {
                String line = in.readLine();
                if (line == null) break;

                LOG.atInfo()
                        .addArgument(line)
                        .addArgument(() -> clientSocket.getInetAddress().toString())
                        .log("Received <{}> from <{}>");

                String[] parts = line.split(" ", 2);
                if (parts.length == 0) {
                    LOG.debug("Split up parts are empty, returning...");
                    break;
                }
                String command = parts[0].toUpperCase();
                String argument = parts.length > 1 ? parts[1] : "";
                Command commandState = CommandRegistry.get(command);
                commandState.handle(argument, context, null);
            }
        } catch (IOException e) {
            LOG.atError().setCause(e).addArgument(e::getMessage).log("Error handling client: <{}>");
            shallAcceptConnections.set(false);
        } finally {
            this.closeIn(in);
            this.closeClientSocket();
        }
    }

    public void quitSession() {
        this.shallAcceptConnections.set(false);
    }


    private void closeClientSocket() {
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            LOG.atError().setCause(e).addArgument(e::getMessage).log("Error closing client: <{}>");
        }
    }

    private void closeIn(BufferedReader in) {
        try {
            if (in != null) in.close();
        } catch (IOException e) {
            LOG.atError().setCause(e).addArgument(e::getMessage).log("Error closing input stream for client: <{}>");
        }
    }
}
