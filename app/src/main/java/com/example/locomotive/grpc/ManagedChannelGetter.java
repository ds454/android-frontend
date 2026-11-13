package com.example.locomotive.grpc;

import android.content.Intent;

import com.example.locomotive.Locomotive;
import com.example.locomotive.LoginActivity;

import java.util.concurrent.atomic.AtomicBoolean;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ManagedChannelGetter {

    private static ManagedChannel channel = null;

    public static void makeNewChannel(String host, int port, boolean useTls) {
        if (channel != null) {
            channel.shutdownNow();
        }

        channel = useTls ?
                ManagedChannelBuilder
                        .forAddress(host, port)
                        .intercept(new SessionTokenInterceptor(SessionTokenManager::getSessionToken))
                        .intercept(new UnauthenticatedInterceptor(Locomotive.getContext()))
                        .usePlaintext()
                        .build()
                :
                ManagedChannelBuilder
                        .forAddress(host, port)
                        .intercept(new SessionTokenInterceptor(SessionTokenManager::getSessionToken))
                        .intercept(new UnauthenticatedInterceptor(Locomotive.getContext()))
                        .useTransportSecurity()
                        .build();
    }

    public static ManagedChannel get() {
        if (channel == null) {
            throw new IllegalStateException();
        }
        return channel;
    }
}
