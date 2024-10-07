package com.httpserver;

import org.slf4j.Logger;
import java.util.Objects;
import java.io.IOException;
import org.slf4j.LoggerFactory;
import com.httpserver.config.HttpServerConfiguration;
import com.httpserver.core.http.HttpServerListenerThread;
import com.httpserver.core.https.HttpsServerListenerThread;
import com.httpserver.config.ConfigurationManager;
import com.httpserver.config.SSLConfiguration;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the HTTP server.
 * This class initializes the server by loading configuration settings
 * and starting the server listener thread.
 */
@SpringBootApplication
public class HttpServerApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServerApplication.class);

    /**
     * The entry point of the HTTP server application.
     *
     * @param args command-line arguments; expects a single argument specifying the configuration file path.
     */
    public static void main(String[] args) {

//		if (args.length != 1) {
//			LOGGER.error("No configuration file provided.");
//			LOGGER.error("Syntax:  java -jar simplehttpserver-1.0-SNAPSHOT.jar  <config.json>");
//			return;
//		}

        LOGGER.info("Starting server...");

        try {
        	loadConfigurations();
        } catch (Exception e) {
            LOGGER.error("Failed to load configuration file: {}", e.getMessage());
            return;
        }
        HttpServerConfiguration config = ConfigurationManager.getInstance().getConfiguration(HttpServerConfiguration.class);

        LOGGER.info("Using HTTP Port: {}", config.getHttpPort());
        LOGGER.info("Using HTTPS Port: {}", config.getHttpsPort());
        LOGGER.info("Using Webroot: {}", config.getWebroot());

        try {
            LOGGER.info("Starting server listener threads...");
            HttpsServerListenerThread serverListenerThread = new HttpsServerListenerThread(config.getHttpsPort() , config.getWebroot());
            serverListenerThread.start();
            
            HttpServerListenerThread httpServerListenerThread = new HttpServerListenerThread(config.getHttpPort() , config.getWebroot());
            httpServerListenerThread.start();
            
            LOGGER.info("Server listener threads started successfully.");
        } catch (Exception e) {
            // TODO Handle Later
            LOGGER.error("Error starting server listener thread: {}", e.getMessage());
        }
    }
    
    /**
     * Load configurations from JSON files.
     * 
     * @throws IOException if configuration files are not found or can't be read.
     */
    private static void loadConfigurations() throws IOException {
        ConfigurationManager.getInstance().loadConfiguration(
            Objects.requireNonNull(HttpServerApplication.class.getClassLoader().getResource("http.json")).getFile(),
            HttpServerConfiguration.class
        );
        ConfigurationManager.getInstance().loadConfiguration(
            Objects.requireNonNull(HttpServerApplication.class.getClassLoader().getResource("ssl-config.json")).getFile(),
            SSLConfiguration.class
        );
    }
}
