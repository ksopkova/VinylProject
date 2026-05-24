package com.example.hellofx.vinyl.server.strategy;

import com.example.hellofx.vinyl.Model.Vinyl;
import com.example.hellofx.vinyl.network.protocol.VinylMessageMapper;
import com.example.hellofx.vinyl.server.RequestContext;

public abstract class AbstractVinylStrategy implements RequestStrategy {
    protected Vinyl findVinyl(RequestContext context, Object vinylId) {
        String id = VinylMessageMapper.stringValue(vinylId);
        return context.getLibrary()
                .findById(id)
                .orElseThrow(() -> new IllegalStateException("Vinyl was not found."));
    }
}
