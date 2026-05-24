package com.example.hellofx.vinyl.ViewModel;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.client.VinylClient;
import com.example.hellofx.vinyl.client.VinylClientListener;
import com.example.hellofx.vinyl.network.protocol.VinylMessageMapper;
import com.example.hellofx.vinyl.server.VinylServer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class MainScreenViewModel {
    private final ObservableList<Vinyl> vinyls = FXCollections.observableArrayList();
    private final ObservableList<String> activityMessages = FXCollections.observableArrayList();
    private final StringProperty status = new SimpleStringProperty("Connecting to server...");
    private final User currentUser;
    private final VinylClient client;

    public MainScreenViewModel() {
        String clientId = "client-" + UUID.randomUUID().toString().substring(0, 8);
        String userName = System.getProperty("user.name", "User");
        currentUser = new User(clientId, userName);
        client = new VinylClient("localhost", VinylServer.port, currentUser);
        client.addListener(new VinylClientListener() {
            @Override
            public void onMessage(Map<String, Object> message) {
                runOnFxThread(() -> handleServerEvent(message));
            }

            @Override
            public void onConnectionClosed(String reason) {
                runOnFxThread(() -> {
                    status.set("Disconnected from server: " + reason);
                    addActivity("Disconnected from server: " + reason);
                });
            }
        });
        connect();
    }

    public ObservableList<Vinyl> getVinyls() {
        return vinyls;
    }

    public ObservableList<String> getActivityMessages() {
        return activityMessages;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String message) {
        status.set(message);
        addActivity(message);
    }

    public void reserve(Vinyl vinyl) {
        sendVinylAction(vinyl, client::reserve, "Select a vinyl first.", "Reserve request sent.");
    }

    public void borrow(Vinyl vinyl) {
        sendVinylAction(vinyl, client::borrow, "Select a vinyl first.", "Borrow request sent.");
    }

    public void returnVinyl(Vinyl vinyl) {
        sendVinylAction(vinyl, client::returnVinyl, "Select a vinyl first.", "Return request sent.");
    }

    public void remove(Vinyl vinyl) {
        sendVinylAction(vinyl, client::remove, "Select a vinyl first.", "Remove request sent.");
    }

    public void reserveAs(Vinyl vinyl, User user) {
        if (vinyl != null) {
            handleFuture(client.reserve(vinyl.getId(), user));
        }
    }

    public void borrowAs(Vinyl vinyl, User user) {
        if (vinyl != null) {
            handleFuture(client.borrow(vinyl.getId(), user));
        }
    }

    public void returnAs(Vinyl vinyl, User user) {
        if (vinyl != null) {
            handleFuture(client.returnVinyl(vinyl.getId(), user));
        }
    }

    private void connect() {
        CompletableFuture.runAsync(() -> {
            try {
                client.connect();
                runOnFxThread(() -> {
                    status.set("Connected as " + currentUser.getUserName() + ".");
                    addActivity("Connected to server.");
                });
                requestVinylList();
            } catch (IOException e) {
                runOnFxThread(() -> {
                    status.set("Could not connect to server on localhost:" + VinylServer.port);
                    addActivity("Connection failed: " + e.getMessage());
                });
            }
        });
    }

    private void requestVinylList() {
        handleFuture(client.listVinyls());
    }

    private void sendVinylAction(Vinyl vinyl,
                                 Function<String, CompletableFuture<Map<String, Object>>> action,
                                 String nullMessage,
                                 String sentMessage) {
        if (vinyl == null) {
            status.set(nullMessage);
            return;
        }
        status.set(sentMessage);
        handleFuture(action.apply(vinyl.getId()));
    }

    private void handleFuture(CompletableFuture<Map<String, Object>> future) {
        future.thenAccept(response -> runOnFxThread(() -> handleResponse(response)))
                .exceptionally(error -> {
                    runOnFxThread(() -> {
                        status.set(error.getMessage());
                        addActivity("Client error: " + error.getMessage());
                    });
                    return null;
                });
    }

    private void handleResponse(Map<String, Object> response) {
        String message = VinylMessageMapper.stringValue(response.get("message"));
        status.set(message);
        addActivity("Server response: " + message);

        if (!Boolean.TRUE.equals(response.get("success"))) {
            return;
        }

        if (response.containsKey("vinyls")) {
            replaceVinylList(response.get("vinyls"));
        }
        if (response.containsKey("vinyl")) {
            updateOrAddVinyl(response.get("vinyl"));
        }
    }

    private void handleServerEvent(Map<String, Object> event) {
        String message = VinylMessageMapper.stringValue(event.get("message"));
        String eventType = VinylMessageMapper.stringValue(event.get("event"));
        addActivity("Broadcast " + eventType + ": " + message);

        if ("VINYL_REMOVED".equals(eventType)) {
            String vinylId = VinylMessageMapper.stringValue(event.get("vinylId"));
            vinyls.removeIf(vinyl -> vinyl.getId().equals(vinylId));
            status.set(message);
            return;
        }

        if (event.containsKey("vinyl")) {
            updateOrAddVinyl(event.get("vinyl"));
            status.set(message);
        }
    }

    @SuppressWarnings("unchecked")
    private void replaceVinylList(Object vinylListValue) {
        vinyls.clear();
        if (!(vinylListValue instanceof List<?> rawList)) {
            return;
        }

        for (Object item : rawList) {
            vinyls.add(VinylMessageMapper.fromMap((Map<String, Object>) item));
        }
    }

    @SuppressWarnings("unchecked")
    private void updateOrAddVinyl(Object vinylValue) {
        Map<String, Object> vinylMap = (Map<String, Object>) vinylValue;
        String id = VinylMessageMapper.stringValue(vinylMap.get("id"));
        for (Vinyl vinyl : vinyls) {
            if (vinyl.getId().equals(id)) {
                VinylMessageMapper.copyInto(vinyl, vinylMap);
                return;
            }
        }
        vinyls.add(VinylMessageMapper.fromMap(vinylMap));
    }

    private void addActivity(String message) {
        activityMessages.add(0, message);
        if (activityMessages.size() > 100) {
            activityMessages.remove(100, activityMessages.size());
        }
    }

    private void runOnFxThread(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }
}
