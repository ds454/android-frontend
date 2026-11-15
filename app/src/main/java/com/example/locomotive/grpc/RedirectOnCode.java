package com.example.locomotive.grpc;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locomotive.Locomotive;

import java.util.EnumSet;
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

public class RedirectOnCode implements ClientInterceptor {

    public interface OnRedirect {
        void onRedirect(Status.Code grpcStatus);
    }

    private static final Handler mainThread = new Handler(Looper.getMainLooper());
    private static final AtomicBoolean redirecting = new AtomicBoolean(false);
    private static final long GLOBAL_REDIRECT_COOLDOWN_MS = 3000;


    private final Context appContext;
    private final Class<? extends AppCompatActivity> redirectActivity;
    private final EnumSet<Status.Code> codes;
    private final int flags;
    private final OnRedirect onRedirect;

    /**
     * Starts a new activity
     * @param codes            Set of codes to match against. If the backend's response code is in this set, the provided activity will start.
     * @param redirectActivity Activity to start if the backend response code is in the provided codes set.
     * @param intentFlags      Flags to pass to intent.addFlags().
     * @param onRedirect       Function that should run when one of the specified response codes is received.
     */
    public RedirectOnCode(EnumSet<Status.Code> codes, Class<? extends AppCompatActivity> redirectActivity, int intentFlags, OnRedirect onRedirect) {
        this.appContext = Locomotive.getContext();
        this.redirectActivity = redirectActivity;
        this.codes = codes;
        this.flags = intentFlags;
        this.onRedirect = onRedirect;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<>(
                next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                super.start(
                        new ForwardingClientCallListener.SimpleForwardingClientCallListener<>(responseListener) {
                            @Override
                            public void onClose(Status status, Metadata trailers) {
                                try {
                                    // If the gRPC response code is in the provided set, and we aren't already redirecting
                                    if (codes.contains(status.getCode()) && redirecting.compareAndSet(false, true)) {

                                        // Allow redirects after a time
                                        mainThread.postDelayed(() -> redirecting.set(false), GLOBAL_REDIRECT_COOLDOWN_MS);

                                        // Call the onRedirect on the main thread
                                        mainThread.post(() -> onRedirect.onRedirect(status.getCode()));

                                        // Redirect to the provided page on the main thread
                                        mainThread.post(() -> appContext.startActivity(new Intent(appContext, redirectActivity).addFlags(flags)));
                                    }
                                }
                                finally {
                                    super.onClose(status, trailers);
                                }
                            }
                        },
                        headers);
            }
        };
    }
}

