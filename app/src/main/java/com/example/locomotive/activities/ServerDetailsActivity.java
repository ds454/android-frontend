package com.example.locomotive.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locomotive.LandingActivity;
import com.example.locomotive.LoginActivity;
import com.example.locomotive.R;
import com.example.locomotive.grpc.ServerDetailsManager;
import com.example.locomotive.util.valid.PortChecker;

public class ServerDetailsActivity extends AppCompatActivity {

    private String serverMethod;
    private String serverHost;
    private Integer serverPort;

    private Button commitServerDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server_details);

        Spinner serverMethodSelector = findViewById(R.id.server_method);
        // Views get distinct names so they no longer shadow the fields above.
        EditText serverHostInput = findViewById(R.id.server_host);
        EditText serverPortInput = findViewById(R.id.server_port);
        commitServerDetails = findViewById(R.id.enter_server);

        // Nothing is valid until the user has filled the form in.
        updateCommitButton();

        serverMethodSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                serverMethod = adapterView.getItemAtPosition(i).toString();
                updateCommitButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                serverMethod = null;
                updateCommitButton();
            }
        });

        serverHostInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String host = s.toString().trim();
                if (host.isEmpty()) {
                    serverHostInput.setError("Server hostname required");
                    serverHost = null;
                } else {
                    serverHostInput.setError(null);
                    serverHost = host;
                }
                updateCommitButton();
            }
        });

        serverPortInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer port;
                try {
                    port = Integer.parseInt(s.toString().trim());
                } catch (NumberFormatException e) {
                    port = null;
                }

                if (port == null) {
                    serverPortInput.setError("Port required");
                    serverPort = null;
                } else {
                    var portProblem = PortChecker.portIsValid(port);
                    if (portProblem.isPresent()) {
                        serverPortInput.setError(portProblem.get());
                        serverPort = null;
                    } else {
                        serverPortInput.setError(null);
                        serverPort = port;
                    }
                }
                updateCommitButton();
            }
        });

        commitServerDetails.setOnClickListener(view -> {
            if (!fieldsAreValid()) {
                return;
            }

            ServerDetailsManager.ServerMethod method;
            switch (serverMethod) {
                case "http://":  method = ServerDetailsManager.ServerMethod.HTTP;  break;
                case "https://": method = ServerDetailsManager.ServerMethod.HTTPS; break;
                default: return; // Unknown scheme; nothing sensible to commit.
            }
            ServerDetailsManager.setServerMethod(method);
            ServerDetailsManager.setHostname(serverHost);
            ServerDetailsManager.setPort(serverPort);

            var toLanding = new Intent(ServerDetailsActivity.this, LandingActivity.class);
            toLanding.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(toLanding);
        });
    }

    private void updateCommitButton() {
        commitServerDetails.setEnabled(fieldsAreValid());
    }

    private boolean fieldsAreValid() {
        return serverMethod != null && serverHost != null && serverPort != null;
    }

    /** TextWatcher with the two unused callbacks stubbed out. */
    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }
}