package com.example.locomotive.grpc;

import android.content.Context;
import android.content.Intent;

import com.example.locomotive.LoginActivity;

import java.util.concurrent.atomic.AtomicBoolean;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;

public class UnauthenticatedInterceptor implements ClientInterceptor {
    private static final AtomicBoolean redirecting = new AtomicBoolean(false);
    private final Context appContext;

    public UnauthenticatedInterceptor(Context ctx) {
        this.appContext = ctx;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                next.newCall(method, callOptions)) {
            // A little indentation, stay with me here.
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                super.start(
                        new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                            @Override
                            public void onClose(Status status, Metadata trailers) {
                                if (status.getCode() == Status.Code.UNAUTHENTICATED) {
                                    // Check if another thread is already redirecting
                                    if (!redirecting.compareAndSet(false, true)) return;

                                    // Clear the saved session token
                                    SessionTokenManager.clear();

                                    // Redirect to the Login Page
                                    var intent = new Intent(appContext, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    appContext.startActivity(intent);

                                    // Allow other threads to redirect as well
                                    redirecting.set(false);
                                }
                                super.onClose(status, trailers);
                            }
                        },
                        headers);
            }
        };
    }
}
