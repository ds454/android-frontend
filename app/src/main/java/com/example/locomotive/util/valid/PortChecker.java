package com.example.locomotive.util.valid;

import java.util.Optional;

public class PortChecker {
    public static Optional<String> portIsValid(int port) {
        if (port <= 0) {
            return Optional.of("Port must be greater than 0");
        }
        if (port > 65535) {
            return Optional.of("Port must be less than 65535");
        }
        return Optional.empty();
    }
}
