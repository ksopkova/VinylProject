package com.example.hellofx.vinyl.server.strategy;

import com.example.hellofx.vinyl.server.RequestContext;

import java.util.Map;

public interface RequestStrategy {
    String action();

    Map<String, Object> handle(RequestContext context, Map<String, Object> request);
}
