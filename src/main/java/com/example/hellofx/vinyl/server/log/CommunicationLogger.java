package com.example.hellofx.vinyl.server.log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class CommunicationLogger {
    private static final CommunicationLogger INSTANCE = new CommunicationLogger();
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path logDirectory = Path.of("logs");

    private CommunicationLogger() {
    }

    public static CommunicationLogger getInstance() {
        return INSTANCE;
    }

    public synchronized void log(String ipAddress, String text) {
        String timestamp = LocalDateTime.now().format(TIME_FORMAT);
        String line = timestamp + " | " + ipAddress + " | " + text;
        System.out.println(line);

        try {
            Files.createDirectories(logDirectory);
            Files.writeString(
                    logDirectory.resolve("server-" + LocalDate.now() + ".log"),
                    line + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Could not write communication log: " + e.getMessage());
        }
    }
}
