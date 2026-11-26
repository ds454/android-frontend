package com.example.locomotive.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locomotive.R;

import ds454.locomotive.generated.grpc.Session;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class AuthSessionAdapter extends RecyclerView.Adapter<AuthSessionAdapter.ViewHolder> {

    public interface AuthSessionLogoutHandler {
        void handleLogout(Session session);
    }

    private final List<Session> sessions;
    private final AuthSessionLogoutHandler logoutHandler;

    public AuthSessionAdapter(List<Session> sessions, AuthSessionLogoutHandler logoutHandler) {
        this.sessions = sessions;
        this.logoutHandler = logoutHandler;
    }

    @NonNull
    @Override
    public AuthSessionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_auth_session,
                                parent,
                                false));
    }

    @Override
    public void onBindViewHolder(@NonNull AuthSessionAdapter.ViewHolder holder, int position) {
        var session = sessions.get(position);

        holder.device.setText(session.hasDevice() ? session.getDevice() : "Unknown Device");

        var exp = DateFormat
                .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                .format(new Date(session.getExpiration() * 1000L));

        holder.expiration.setText(exp);

        holder.logout.setOnClickListener(view -> logoutHandler.handleLogout(session));
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public void removeSessionFromList(Session s) {
        int sessionIndex = sessions.indexOf(s);
        if (sessionIndex != -1) {
            sessions.remove(sessionIndex);
            notifyItemRemoved(sessionIndex);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView device;
        final TextView expiration;
        final ImageButton logout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            device = itemView.findViewById(R.id.auth_session_devname);
            expiration = itemView.findViewById(R.id.auth_session_expiration);
            logout = itemView.findViewById(R.id.auth_logout_button);
        }
    }
}
