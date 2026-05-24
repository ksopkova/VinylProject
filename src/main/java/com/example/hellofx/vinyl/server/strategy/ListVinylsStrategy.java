package com.example.hellofx.vinyl.server.strategy;

import com.example.hellofx.vinyl.server.RequestContext;
import com.example.hellofx.vinyl.server.ServerMessages;

import java.util.Map;

public class ListVinylsStrategy implements RequestStrategy {
    @Override
    public String action() {
        return "list";
    }

    @Override
    public Map<String, Object> handle(RequestContext context, Map<String, Object> request) {
        return ServerMessages.listResponse(request, context.getLibrary().getVinylSnapshot());
    }
}
