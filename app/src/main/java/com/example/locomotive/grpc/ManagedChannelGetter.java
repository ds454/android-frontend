package com.example.locomotive.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ManagedChannelGetter {

    private static ManagedChannel channel = null;

    public static ManagedChannel get() {
        if (channel == null) {
            // 10.0.2.2 is how the android emulator gets to localhost
            // Entry listens on localhost:8000, and we use plain text. Dev and prod is
            // ready for TLS, but for local development, plaintext is the way to go.
            channel = ManagedChannelBuilder
                    .forAddress("10.0.2.2", 8000)
                    .intercept(new SessionTokenInterceptor(SessionTokenManager::getSessionToken))
                    .intercept(new UnauthenticatedInterceptor(() -> {
                        // This runs every time the user isn't authenticated

                    }))
                    .usePlaintext()
                    .build();
        }
        return channel;
    }
}
