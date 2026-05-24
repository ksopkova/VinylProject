package com.example.hellofx.vinyl.server;

import com.example.hellofx.vinyl.Model.Library;

public class RequestContext {
    private final Library library;
    private final ClientHandler client;

    public RequestContext(Library library, ClientHandler client) {
        this.library = library;
        this.client = client;
    }

    public Library getLibrary() {
        return library;
    }

    public ClientHandler getClient() {
        return client;
    }
}
