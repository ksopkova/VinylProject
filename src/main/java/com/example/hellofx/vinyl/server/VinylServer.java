package com.example.hellofx.vinyl.server;

import com.example.hellofx.vinyl.Model.DemoLibraryFactory;
import com.example.hellofx.vinyl.Model.Library;
import com.example.hellofx.vinyl.Model.Observer.LibraryEvent;
import com.example.hellofx.vinyl.Model.Observer.LibraryObserver;
import com.example.hellofx.vinyl.network.protocol.VinylMessageMapper;
import com.example.hellofx.vinyl.server.log.CommunicationLogger;
import com.example.hellofx.vinyl.server.strategy.BorrowVinylStrategy;
import com.example.hellofx.vinyl.server.strategy.ListVinylsStrategy;
import com.example.hellofx.vinyl.server.strategy.RemoveVinylStrategy;
import com.example.hellofx.vinyl.server.strategy.RequestStrategy;
import com.example.hellofx.vinyl.server.strategy.ReserveVinylStrategy;
import com.example.hellofx.vinyl.server.strategy.ReturnVinylStrategy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class VinylServer implements LibraryObserver {
    public static final int port = 5555;
    private final Library library;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final Map<String, RequestStrategy> strategies = new ConcurrentHashMap<>();
    private final CommunicationLogger logger = CommunicationLogger.getInstance();

    public VinylServer() {
        this.library = DemoLibraryFactory.createLibrary();
        this.library.addObserver(this);
        register(new ListVinylsStrategy());
        register(new ReserveVinylStrategy());
        register(new BorrowVinylStrategy());
        register(new ReturnVinylStrategy());
        register(new RemoveVinylStrategy());
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.log("SERVER", "Vinyl server started on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);
                Thread thread = new Thread(handler, "client-" + handler.getIpAddress());
                thread.start();
            }
        }
    }

    public Map<String, Object> handleRequest(ClientHandler client, Map<String, Object> request) {
        try {
            String action = VinylMessageMapper.stringValue(request.get("action"));
            RequestStrategy strategy = strategies.get(action);
            if (strategy == null) {
                return ServerMessages.error(request, "Unknown action: " + action);
            }
            return strategy.handle(new RequestContext(library, client), request);
        } catch (IllegalStateException e) {
            return ServerMessages.error(request, e.getMessage());
        } catch (Exception e) {
            return ServerMessages.error(request, "Server error: " + e.getMessage());
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    @Override
    public void onLibraryChanged(LibraryEvent event) {
        broadcast(ServerMessages.event(event));
    }

    private void register(RequestStrategy strategy) {
        strategies.put(strategy.action(), strategy);
    }

    private void broadcast(Map<String, Object> event) {
        for (ClientHandler client : clients) {
            client.send(event);
        }
    }
}
