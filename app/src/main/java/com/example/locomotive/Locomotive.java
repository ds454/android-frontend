package com.example.locomotive;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locomotive.activities.ServerDetailsActivity;
import com.example.locomotive.grpc.ManagedChannelGetter;
import com.example.locomotive.grpc.ServerDetailsManager;
import com.example.locomotive.grpc.SessionTokenManager;

public class Locomotive extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        SessionTokenManager.init(appContext);
        ServerDetailsManager.init(appContext);

        if (ServerDetailsManager.serverDetailsAreSet()) {
            // Set up the gRPC channel
            var hostname = ServerDetailsManager.getHostname().get();
            var port = ServerDetailsManager.getPort().get();
            var useTls = ServerDetailsManager.getServerMethod().get() == ServerDetailsManager.ServerMethod.HTTPS;
            ManagedChannelGetter.makeNewChannel(hostname, port, useTls);

            redirectTo(LandingActivity.class);
        }
        else {
            redirectTo(ServerDetailsActivity.class);
        }
    }

    private void redirectTo(Class<? extends AppCompatActivity> clazz) {
        var intent = new Intent(Locomotive.this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public static Context getContext() {
        return appContext;
    }
}
