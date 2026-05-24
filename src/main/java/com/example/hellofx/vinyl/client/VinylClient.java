package com.example.hellofx.vinyl.client;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.network.protocol.JsonCodec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class VinylClient {
    private final String host;
    private final int port;
    private final User user;
    private final Map<String, CompletableFuture<Map<String, Object>>> pendingRequests = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<VinylClientListener> listeners = new CopyOnWriteArrayList<>();

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private volatile boolean connected;

    public VinylClient(String host, int port, User user) {
        this.host = host;
        this.port = port;
        this.user = user;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        connected = true;
        Thread listenerThread = new Thread(this::listen, "vinyl-client-listener");
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public CompletableFuture<Map<String, Object>> listVinyls() {
        return sendRequest("list", null);
    }

    public CompletableFuture<Map<String, Object>> reserve(String vinylId) {
        return sendRequest("reserve", vinylId, user);
    }

    public CompletableFuture<Map<String, Object>> reserve(String vinylId, User requestUser) {
        return sendRequest("reserve", vinylId, requestUser);
    }

    public CompletableFuture<Map<String, Object>> borrow(String vinylId) {
        return sendRequest("borrow", vinylId, user);
    }

    public CompletableFuture<Map<String, Object>> borrow(String vinylId, User requestUser) {
        return sendRequest("borrow", vinylId, requestUser);
    }

    public CompletableFuture<Map<String, Object>> returnVinyl(String vinylId) {
        return sendRequest("return", vinylId, user);
    }

    public CompletableFuture<Map<String, Object>> returnVinyl(String vinylId, User requestUser) {
        return sendRequest("return", vinylId, requestUser);
    }

    public CompletableFuture<Map<String, Object>> remove(String vinylId) {
        return sendRequest("remove", vinylId);
    }

    public void addListener(VinylClientListener listener) {
        listeners.add(listener);
    }

    public void removeListener(VinylClientListener listener) {
        listeners.remove(listener);
    }

    public boolean isConnected() {
        return connected;
    }

    public void close() {
        connected = false;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    private CompletableFuture<Map<String, Object>> sendRequest(String action, String vinylId) {
        return sendRequest(action, vinylId, user);
    }

    private CompletableFuture<Map<String, Object>> sendRequest(String action, String vinylId, User requestUser) {
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        if (!connected || writer == null) {
            future.completeExceptionally(new IllegalStateException("Client is not connected to the server."));
            return future;
        }

        String requestId = UUID.randomUUID().toString();
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("kind", "request");
        request.put("requestId", requestId);
        request.put("action", action);
        request.put("userId", requestUser.getUserID());
        request.put("userName", requestUser.getUserName());
        if (vinylId != null) {
            request.put("vinylId", vinylId);
        }

        pendingRequests.put(requestId, future);
        try {
            synchronized (this) {
                writer.write(JsonCodec.stringify(request));
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            pendingRequests.remove(requestId);
            future.completeExceptionally(e);
        }
        return future;
    }

    private void listen() {
        try {
            String line;
            while (connected && (line = reader.readLine()) != null) {
                Map<String, Object> message = JsonCodec.parseObject(line);
                if ("response".equals(message.get("kind"))) {
                    completePendingRequest(message);
                } else {
                    notifyMessage(message);
                }
            }
            notifyClosed("Server connection closed.");
        } catch (Exception e) {
            notifyClosed(e.getMessage());
        } finally {
            connected = false;
            completeAllPending("Connection closed.");
        }
    }

    private void completePendingRequest(Map<String, Object> message) {
        Object requestId = message.get("requestId");
        CompletableFuture<Map<String, Object>> future = pendingRequests.remove(String.valueOf(requestId));
        if (future != null) {
            future.complete(message);
        }
    }

    private void completeAllPending(String reason) {
        for (CompletableFuture<Map<String, Object>> future : pendingRequests.values()) {
            future.completeExceptionally(new IllegalStateException(reason));
        }
        pendingRequests.clear();
    }

    private void notifyMessage(Map<String, Object> message) {
        for (VinylClientListener listener : listeners) {
            listener.onMessage(message);
        }
    }

    private void notifyClosed(String reason) {
        for (VinylClientListener listener : listeners) {
            listener.onConnectionClosed(reason);
        }
    }
}
