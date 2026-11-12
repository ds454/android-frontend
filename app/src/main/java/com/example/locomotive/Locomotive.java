package com.example.locomotive;

import android.app.Application;
import android.content.Context;

import com.example.locomotive.grpc.SessionTokenManager;

public class Locomotive extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        SessionTokenManager.init(appContext);
    }

    public static Context getContext() {
        return appContext;
    }
}
