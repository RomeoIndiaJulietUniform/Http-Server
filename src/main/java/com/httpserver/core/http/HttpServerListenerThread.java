package com.httpserver.core.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents a server listener thread that accepts incoming HTTP connections.
 * This class extends Thread to manage the lifecycle of the server socket and
 * handle client requests by spawning worker threads.
 */
public class HttpServerListenerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServerListenerThread.class);

    private final int port;
    private final ServerSocket serverSocket;
    private final String webroot;

    /**
     * Constructs a HttpServerListenerThread with the specified port and web root.
     *
     * @param port    the port on which the server will listen for incoming
     *                connections
     * @param webroot the root directory for serving web content
     * @throws IOException if an I/O error occurs when opening the server socket
     */
    public HttpServerListenerThread(int port, String webroot) throws IOException {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
        LOGGER.debug("HTTP - Server initialized on port: {} with webroot: {}", this.port, this.webroot);
    }

    /**
     * Returns the port number of HTTP Server.
     *
     * @return the port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the web root directory for serving files.
     *
     * @return the web root directory
     */
    public String getWebroot() {
        return webroot;
    }

    /**
     * Runs the server listener thread, accepting incoming connections and spawning
     * worker threads to handle them.
     * <p>
     * This method loops indefinitely until the server socket is closed, accepting
     * client connections and creating an instance of
     * {@link HttpConnectionWorkerThread} for each accepted socket connection. It
     * logs connection information, including the port and the client's IP address,
     * and handles any IOExceptions that may occur during socket operations.
     * </p>
     *
     * @throws IOException if an I/O error occurs while waiting for a connection
     */
    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                LOGGER.debug("Waiting for a new connection...");
                Socket socket = serverSocket.accept(); // Code waits here until the connection is accepted.

                LOGGER.info("* Connection accepted on port {}", this.port);
                LOGGER.info("* Connection accepted from IP: {}", socket.getInetAddress());

                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start();
            }
        } catch (IOException e) {
            LOGGER.error("Problem with setting socket: {}", e.getMessage());
            LOGGER.debug("Full stack trace:", e); // Log the stack trace for debugging
        } finally {
            try {
                serverSocket.close();
                LOGGER.debug("Server socket closed successfully.");
            } catch (IOException e) {
                LOGGER.error("Error in closing ServerSocket in HttpServerListenerThread: {}", e.getMessage());
                LOGGER.debug("Full stack trace:", e); // Log the stack trace for debugging
            }
        }
    }
}
