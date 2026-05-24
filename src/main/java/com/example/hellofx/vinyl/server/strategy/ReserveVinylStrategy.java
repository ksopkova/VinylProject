package com.example.hellofx.vinyl.server.strategy;

import com.example.hellofx.vinyl.Model.User;
import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.network.protocol.VinylMessageMapper;
import com.example.hellofx.vinyl.server.RequestContext;
import com.example.hellofx.vinyl.server.ServerMessages;

import java.util.Map;

public class ReserveVinylStrategy extends AbstractVinylStrategy {
    @Override
    public String action() {
        return "reserve";
    }

    @Override
    public Map<String, Object> handle(RequestContext context, Map<String, Object> request) {
        Vinyl vinyl = findVinyl(context, request.get("vinylId"));
        User user = VinylMessageMapper.userFromRequest(request);
        context.getLibrary().reserve(vinyl, user);
        return ServerMessages.vinylResponse(request, "Vinyl reserved.", vinyl);
    }
}
