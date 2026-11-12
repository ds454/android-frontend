package com.example.locomotive.grpc;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Optional;

// Manages the logged-in user's session token. Shouldn't need to be used outside
// LoginActivity and ManagedChannelGetter.
public class SessionTokenManager {
    private static final String sessionPreferences = "sessionPreferences";
    private static final String sessionTokenKey = "tkn";

    private static SharedPreferences preferences;

    public static void init(Context ctx) {
        preferences = ctx
                .getApplicationContext()
                .getSharedPreferences(sessionPreferences, Context.MODE_PRIVATE);
    }

    public static void saveSessionToken(String sessionTokenValue) {
        preferences.edit().putString(sessionTokenKey, sessionTokenValue).apply();
    }

    public static String getSessionToken() {
        return preferences.getString(sessionTokenKey, null);
    }

    public static void clear() {
        preferences.edit().remove(sessionTokenKey).apply();
    }
}
