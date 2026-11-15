package com.example.locomotive;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locomotive.activities.ServerDetailsActivity;
import com.example.locomotive.grpc.LocomotiveManagedChannel;
import com.example.locomotive.data.ServerDetailsManager;
import com.example.locomotive.data.SessionTokenManager;

public class Locomotive extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        SessionTokenManager.init(appContext);
        ServerDetailsManager.init(appContext);

        // If the server details are set:
        if (ServerDetailsManager.serverDetailsAreSet()) {

            // Try making the channel, and ping the backend. If the ping fails, the server is down and the server details may be wrong.
            if (!LocomotiveManagedChannel.makeNewChannel(ServerDetailsManager.getServerDetails())) {
                redirectTo(ServerDetailsActivity.class);
                return;
            }

            // If the backend is valid and up but we don't have a session
            // token, this is the first time connecting to this backend, so we
            // need to log in.
            if (!SessionTokenManager.hasSessionToken()) {
                redirectTo(LandingActivity.class);
                return;
            }

            // If we do have a session token, we need to check if it's valid.
            var authStub = ds454.locomotive.generated.grpc.AuthGrpc.newBlockingStub(LocomotiveManagedChannel.get());
            var response = authStub.checkIfSessionIsValid(ds454.locomotive.generated.grpc.CheckIfSessionIsValidRequest.newBuilder().build());

            // If the session isn't valid, send them to signup/login
            if (!response.getIsValid()) {
                redirectTo(LandingActivity.class);
                return;
            }

            // If the session is valid, send them to the home page
            redirectTo(HomeActivity.class);
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
