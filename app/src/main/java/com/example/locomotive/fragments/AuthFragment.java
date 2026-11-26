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

import com.example.locomotive.R;
import com.example.locomotive.adapter.AuthSessionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthFragment extends Fragment implements AuthSessionAdapter.AuthSessionLogoutHandler {

    private final List<Session> sessions = new ArrayList<>();
    private AuthSessionAdapter adapter;

    private final Handler main = new Handler(Looper.getMainLooper());

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
    public void handleLogout(Session session) {

    }
}