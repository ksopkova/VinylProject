package com.example.hellofx.vinyl.server.strategy;

import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.server.RequestContext;
import com.example.hellofx.vinyl.server.ServerMessages;

import java.util.Map;

public class RemoveVinylStrategy extends AbstractVinylStrategy {
    @Override
    public String action() {
        return "remove";
    }

    @Override
    public Map<String, Object> handle(RequestContext context, Map<String, Object> request) {
        Vinyl vinyl = findVinyl(context, request.get("vinylId"));
        context.getLibrary().remove(vinyl);
        return ServerMessages.success(request, "Remove request handled.");
    }
}
