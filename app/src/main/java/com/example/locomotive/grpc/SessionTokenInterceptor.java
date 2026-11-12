package com.example.locomotive.grpc;

import java.util.function.Supplier;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

// Adds the session token to outgoing requests.
public class SessionTokenInterceptor implements ClientInterceptor {

    private static final Metadata.Key<String> sessionTokenHeader = Metadata
            .Key
            .of("x-session-token", Metadata.ASCII_STRING_MARSHALLER);

    private final Supplier<String> sessionTokenSupplier;

    public SessionTokenInterceptor(Supplier<String> tokenSupplier) {
        this.sessionTokenSupplier = tokenSupplier;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(
                next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                // Load the token we should have saved earlier from the app's context
                String token = sessionTokenSupplier.get();

                // Only add the session token to the request it if the token exists.
                // Should only be for logging in and signing up.
                if (token != null && !token.isEmpty()) {
                    headers.put(sessionTokenHeader, token);
                }

                // Continue the interceptor chain
                super.start(responseListener, headers);
            }
        };
    }
}
