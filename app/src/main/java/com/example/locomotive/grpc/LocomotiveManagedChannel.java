package com.example.locomotive.grpc;

import android.content.Intent;
import android.widget.Toast;

import com.example.locomotive.Locomotive;
import com.example.locomotive.LoginActivity;
import com.example.locomotive.activities.ServerDetailsActivity;
import com.example.locomotive.data.ServerDetailsManager;
import com.example.locomotive.data.SessionTokenManager;

import java.util.EnumSet;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;

public class LocomotiveManagedChannel {

    private static ManagedChannel channel = null;

    private static final int newTaskFlags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK;

    public static boolean makeNewChannel(ServerDetailsManager.ServerDetails details) {
        return makeNewChannel(details.host, details.port, details.method == ServerDetailsManager.ServerMethod.HTTPS);
    }

    public static boolean makeNewChannel(String host, int port, boolean useTls) {
        // If the old channel exists, shut it down since we're making a new one
        if (channel != null) {
            channel.shutdownNow();
            channel = null;
        }

        // Ping the selected backend to make sure it's up
        var pingChannelBuilder = ManagedChannelBuilder.forAddress(host, port);
        if (useTls) pingChannelBuilder.useTransportSecurity();
        else pingChannelBuilder.usePlaintext();
        var pingChannel = pingChannelBuilder.build();
        try {
            var response = ds454.locomotive.generated.grpc.AuthGrpc.newBlockingStub(pingChannel).ping(ds454.locomotive.generated.grpc.PingRequest.newBuilder().build());
            if (!response.getPong().equals("pong")) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        var builder = ManagedChannelBuilder
                // Host and port of the backend
                .forAddress(host, port)
                // Add the session token to each request
                .intercept(new SessionTokenInterceptor(SessionTokenManager::getSessionToken))
                // Redirect to LoginActivity if we're unauthenticated
                .intercept(new RedirectOnCode(
                        EnumSet.of(Status.Code.UNAUTHENTICATED),
                        LoginActivity.class,
                        newTaskFlags,
                        grpcStatus -> Toast.makeText(
                                Locomotive.getContext(),
                                "Please login again",
                                Toast.LENGTH_LONG).show()))
                // Redirect to ServerDetailsActivity if there was a problem connecting to the backend
                .intercept(new RedirectOnCode(
                        EnumSet.of(Status.Code.UNAVAILABLE, Status.Code.DEADLINE_EXCEEDED),
                        ServerDetailsActivity.class,
                        newTaskFlags,
                        grpcStatus -> Toast.makeText(
                                Locomotive.getContext(),
                                "Unable to connect to your Locomotive backend",
                                Toast.LENGTH_LONG).show()
                ));

        // Optionally use TLS
        if (useTls) builder.useTransportSecurity();
        else builder.usePlaintext();

        // And build the managed channel
        channel = builder.build();

        return true;
    }

    public static ManagedChannel get() {
        if (channel == null) {
            throw new IllegalStateException();
        }
        return channel;
    }
}
