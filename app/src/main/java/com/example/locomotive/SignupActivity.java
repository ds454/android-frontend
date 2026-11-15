package com.example.locomotive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.locomotive.grpc.LocomotiveManagedChannel;
import com.example.locomotive.util.IntentFlags;

import java.util.Optional;

import ds454.locomotive.generated.grpc.AddUserRequest;

/*
    Sign Up Activity:

    Screen Click Through:
    - Traveled to via Landing or Login page
    - Can go to Login or (by signing up) to Home Page

    Functionality:
    - Takes in sign up information (username, password, and steam) from user
    - Sends sign up info to create/establish new user

    TODO: IMPORTANT!!! how to interact with grpc stuff

        import io.grpc.ManagedChannel;
        import io.grpc.okhttp.OkHttpChannelBuilder;

        ManagedChannel c = OkHttpChannelBuilder.forAddress("127.0.0.1", 8000).build();

        //send this
        var loginRequest = ds454.locomotive.generated.grpc.LoginRequest.newBuilder().setUsername("user").setPassword("passwd").setDeviceType("android").build();

        //recieved response
        var loginResponse = ds454.locomotive.generated.grpc.LoginResponse.newBuilder().build();

        var sessionToken = loginResponse.getSessionToken();

 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    //Declare Buttons
    private Button SignupButton;
    private EditText username;
    private EditText password;
    private EditText steamId;

    private ds454.locomotive.generated.grpc.AddUserServiceGrpc.AddUserServiceBlockingStub addUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Init the stub
        addUserService = ds454.locomotive.generated.grpc.AddUserServiceGrpc.newBlockingStub(LocomotiveManagedChannel.get());

        //Initialize Buttons
        SignupButton = findViewById(R.id.signupButton);
        username = findViewById(R.id.UsernameTextEntry);
        password = findViewById(R.id.PasswordTextEntry);
        steamId = findViewById(R.id.SteamTextEntry);

        //Set up listener
        SignupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // grab id of clicked button
        int id = v.getId();

        // If sign up, go to the login up page
        if (id == R.id.signupButton){
            clearAllErrors();

            var provUsername = username.getText().toString();
            var provPassword = password.getText().toString();
            var provSteamId = steamId.getText().toString();

            var signupProblem = signUp(provUsername, provPassword, provSteamId);
            // Signed up without no problem
            if (signupProblem.isEmpty()) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class).setFlags(IntentFlags.ClearStack));
                return;
            }

            var problem = signupProblem.get();
            var message = problem.getMessage();

            // There was a problem signing up
            switch (problem.getField()) {
                case "username":
                    username.setError(message);
                    break;

                case "password":
                    password.setError(message);
                    break;

                case "steamAccountId":
                    steamId.setError(message);
                    break;

                default:
                    Toast.makeText(this, signupProblem.get().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (id == R.id.LoginButton) {
            startActivity(new Intent(SignupActivity.this , LoginActivity.class));
        }
    }

    private Optional<ds454.locomotive.generated.grpc.problem.Problem> signUp(String username, String password, String steamId) {

        var registerUserRequest = AddUserRequest
                .newBuilder()
                .setUsername(username)
                .setPassword(password)
                .setSteamAccountId(steamId)
                .build();

        var response = addUserService.addUser(registerUserRequest);
        if (response.hasProblem()) {
            return Optional.of(response.getProblem());
        }

        return Optional.empty();
    }

    private void clearAllErrors() {
        username.setError(null);
        password.setError(null);
        steamId.setError(null);
    }
}