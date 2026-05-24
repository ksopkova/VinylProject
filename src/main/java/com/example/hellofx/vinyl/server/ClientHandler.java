package com.example.hellofx.vinyl.server;

import com.example.hellofx.vinyl.network.protocol.JsonCodec;
import com.example.hellofx.vinyl.server.log.CommunicationLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final VinylServer server;
    private final CommunicationLogger logger = CommunicationLogger.getInstance();
    private final String ipAddress;

    private BufferedReader reader;
    private BufferedWriter writer;
    private volatile boolean running = true;

    public ClientHandler(Socket socket, VinylServer server) {
        this.socket = socket;
        this.server = server;
        this.ipAddress = socket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        try (Socket ignored = socket) {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            logger.log(ipAddress, "CONNECTED");

            String line;
            while (running && (line = reader.readLine()) != null) {
                logger.log(ipAddress, "IN " + line);
                try {
                    Map<String, Object> request = JsonCodec.parseObject(line);
                    send(server.handleRequest(this, request));
                } catch (Exception e) {
                    send(ServerMessages.error(null, "Invalid JSON request: " + e.getMessage()));
                }
            }
        } catch (Exception e) {
            logger.log(ipAddress, "DISCONNECTED " + e.getMessage());
        } finally {
            running = false;
            server.removeClient(this);
            logger.log(ipAddress, "CLOSED");
        }
    }

    public synchronized void send(Map<String, Object> message) {
        if (!running || writer == null) {
            return;
        }
        try {
            String json = JsonCodec.stringify(message);
            writer.write(json);
            writer.newLine();
            writer.flush();
            logger.log(ipAddress, "OUT " + json);
        } catch (IOException e) {
            running = false;
            logger.log(ipAddress, "SEND FAILED " + e.getMessage());
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
