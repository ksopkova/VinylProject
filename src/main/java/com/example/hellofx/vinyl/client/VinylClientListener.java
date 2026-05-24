package com.example.hellofx.vinyl.client;

import java.util.Map;

public interface VinylClientListener {
    void onMessage(Map<String, Object> message);

    void onConnectionClosed(String reason);
}
