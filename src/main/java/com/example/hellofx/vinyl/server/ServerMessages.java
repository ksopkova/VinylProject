package com.example.hellofx.vinyl.server;

import com.example.hellofx.vinyl.Model.Observer.LibraryEvent;
import com.example.hellofx.vinyl.Model.Observer.LibraryEventType;
import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.network.protocol.VinylMessageMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ServerMessages {
    private ServerMessages() {
    }

    public static Map<String, Object> success(Map<String, Object> request, String message) {
        Map<String, Object> response = baseResponse(request, true, message);
        return response;
    }

    public static Map<String, Object> error(Map<String, Object> request, String message) {
        return baseResponse(request, false, message);
    }

    public static Map<String, Object> listResponse(Map<String, Object> request, List<Vinyl> vinyls) {
        Map<String, Object> response = success(request, "Vinyl list loaded.");
        response.put("vinyls", vinyls.stream().map(VinylMessageMapper::toMap).toList());
        return response;
    }

    public static Map<String, Object> vinylResponse(Map<String, Object> request, String message, Vinyl vinyl) {
        Map<String, Object> response = success(request, message);
        response.put("vinyl", VinylMessageMapper.toMap(vinyl));
        return response;
    }

    public static Map<String, Object> event(LibraryEvent event) {
        Map<String, Object> message = new LinkedHashMap<>();
        message.put("kind", "event");
        message.put("event", event.getType().name());
        message.put("message", event.getMessage());
        message.put("vinylId", event.getVinyl().getId());
        if (event.getType() != LibraryEventType.VINYL_REMOVED) {
            message.put("vinyl", VinylMessageMapper.toMap(event.getVinyl()));
        }
        return message;
    }

    private static Map<String, Object> baseResponse(Map<String, Object> request, boolean success, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("kind", "response");
        response.put("requestId", request == null ? "" : VinylMessageMapper.stringValue(request.get("requestId")));
        response.put("success", success);
        response.put("message", message);
        return response;
    }
}
