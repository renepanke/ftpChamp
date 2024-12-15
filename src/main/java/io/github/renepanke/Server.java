package io.github.renepanke;

import io.github.renepanke.exceptions.FTPServerRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private static AtomicBoolean shallRun;
    private static volatile ExecutorService threadPool;
    public static final int DEFAULT_PORT = 21;
    private final ServerSocket socket;

    public Server() {
        try {
            this.socket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            throw new FTPServerRuntimeException(e);
        }
    }

    public void start() {
        shallRun = new AtomicBoolean(true);
        // TODO:    Investigate if VirtualThreads are feasible for this.
        threadPool = Executors.newFixedThreadPool(10);
        while (shallRun.get()) {
            try {
                Socket clientSocket = this.socket.accept();
                LOG.atInfo().addArgument(clientSocket::getInetAddress).log("Client connected: <{}>");
                threadPool.submit(new RequestHandler(clientSocket));
            } catch (IOException e) {
                LOG.atError().setCause(e).addArgument(e::getMessage).log("Error accepting client connection: {}");
            }
        }
    }

    public void stop() {
        try {
            LOG.info("Shutting down FTP server...");
            LOG.debug("Stop listening for connections");
            if (shallRun != null && shallRun.get()) {
                shallRun.set(false);
            }
            LOG.debug("Stopped listening for connections");
        }
        finally {
            if (threadPool != null) {
                LOG.debug("Shutting down thread pool");
                threadPool.shutdown();
                LOG.debug("Shut down thread pool");
                LOG.debug("Closing thread pool");
                threadPool.close();
                LOG.debug("Closed thread pool");
            }
        }


    }
}
