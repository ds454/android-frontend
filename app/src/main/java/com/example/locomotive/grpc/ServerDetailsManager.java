package com.example.locomotive.grpc;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Optional;

public class ServerDetailsManager {
    private static final String serverDetailsNamespace = "serverDetails";
    private static final String serverMethodKey = "method";
    private static final String serverHostnameKey = "hostname";
    private static final String serverPortKey = "port";

    public enum ServerMethod {
        HTTP,
        HTTPS;

        public static ServerMethod fromString(String str) {
            switch (str) {
                case "HTTP": return ServerMethod.HTTP;
                case "HTTPS": return ServerMethod.HTTPS;
                default: return null;
            }
        }
    }

    private static SharedPreferences serverDetails;

    public static void init(@NonNull Context ctx) {
        serverDetails = ctx
                .getApplicationContext()
                .getSharedPreferences(serverDetailsNamespace, Context.MODE_PRIVATE);
    }

    public static void setServerMethod(@NonNull ServerMethod method) {
        serverDetails.edit().putString(serverMethodKey, method.toString()).apply();
    }

    public static Optional<ServerMethod> getServerMethod() {
        var stringMethod = serverDetails.getString(serverMethodKey, null);
        if (stringMethod == null) {
            return Optional.empty();
        }
        ServerMethod method = ServerMethod.fromString(stringMethod);
        if (method == null) return Optional.empty();
        return Optional.of(method);
    }

    public static void setHostname(@NonNull String hostname) {
        serverDetails.edit().putString(serverHostnameKey, hostname).apply();
    }

    public static Optional<String> getHostname() {
        var prefHostname = serverDetails.getString(serverHostnameKey, null);
        if (prefHostname == null) return Optional.empty();
        return Optional.of(prefHostname);
    }

    public static void setPort(@NonNull int port) {
        serverDetails.edit().putInt(serverPortKey, port).apply();
    }

    public static Optional<Integer> getPort() {
        var prefPort = serverDetails.getInt(serverPortKey, -1);
        if (prefPort == -1) return Optional.empty();
        return Optional.of(prefPort);
    }

    public static boolean serverDetailsAreSet() {
        return getPort().isPresent() && getHostname().isPresent() && getServerMethod().isPresent();
    }
}
