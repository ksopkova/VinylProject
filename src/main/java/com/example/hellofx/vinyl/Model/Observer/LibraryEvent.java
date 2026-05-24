package com.example.hellofx.vinyl.Model.Observer;

import com.example.hellofx.vinyl.Model.Vinyl;

public class LibraryEvent {
    private final LibraryEventType type;
    private final Vinyl vinyl;
    private final String message;

    public LibraryEvent(LibraryEventType type, Vinyl vinyl, String message) {
        this.type = type;
        this.vinyl = vinyl;
        this.message = message;
    }

    public LibraryEventType getType() {
        return type;
    }

    public Vinyl getVinyl() {
        return vinyl;
    }

    public String getMessage() {
        return message;
    }
}
