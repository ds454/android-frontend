package com.example.locomotive.fragments;

import ds454.locomotive.generated.grpc.Session;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.locomotive.R;
import com.example.locomotive.adapter.AuthSessionAdapter;
import com.example.locomotive.grpc.LocomotiveManagedChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthFragment extends Fragment implements AuthSessionAdapter.AuthSessionLogoutHandler {

    private final ds454.locomotive.generated.grpc.AuthGrpc.AuthBlockingStub stub = ds454.locomotive.generated.grpc.AuthGrpc.newBlockingStub(LocomotiveManagedChannel.get());

    private final List<Session> sessions = new ArrayList<>();
    private AuthSessionAdapter adapter;

    private final Handler main = new Handler(Looper.getMainLooper());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public AuthFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AuthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.auth_sessions_container);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        this.adapter = new AuthSessionAdapter(sessions, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadSessions();
    }

    @Override
    public void handleLogout(Session session) {
        executor.execute(() -> {
            var response = stub.logoutSomewhere(ds454.locomotive.generated.grpc.LogoutSomewhereRequest.newBuilder().setSessionTokenId(session.getSessionTokenId()).build());
            runOnUi(() -> {
                if (response.hasProblem()) toast(response.getProblem().getMessage());
                else adapter.removeSessionFromList(session);
            });
        });
    }

    private void loadSessions() {
        executor.execute(() -> {
            var sessions = getImmutableSessions();

            runOnUi(() -> {
                this.sessions.clear();
                this.sessions.addAll(sessions);
                adapter.notifyDataSetChanged();
            });
        });
    }

    private List<Session> getImmutableSessions() {
        var response = stub.getActiveSessions(ds454.locomotive.generated.grpc.GetActiveSessionsRequest.getDefaultInstance());
        return response.getSessions().getSessionsList();
    }

    private void runOnUi(Runnable action) {
        main.post(() -> {
            if (isAdded() && getView() != null) {
                action.run();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

    private void toast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}