package com.example.locomotive;

import ds454.locomotive.generated.grpc.problem.Problem;
import ds454.locomotive.generated.grpc.problem.ProblemType;
import io.grpc.ManagedChannel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;

import com.example.locomotive.grpc.ManagedChannelGetter;
import com.example.locomotive.grpc.SessionTokenManager;
import com.example.locomotive.util.Result;

import ds454.locomotive.generated.grpc.LoginRequest;
import ds454.locomotive.generated.grpc.AuthGrpc;
import ds454.locomotive.generated.grpc.problem.Problem;

/*
    Login Activity:

    Screen Click Through:
    - Traveled to via Landing or Sign Up page
    - Can go to Sign Up or (using log in info) to Home Page

    Functionality:
    - Takes in login info (username and password) from user)
    - Sends info to verify user and get user details

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Declare Buttons
    private Button SignupButton;

    private ManagedChannel c;
    private AuthGrpc.AuthBlockingStub auth;
    private EditText UsernameTextEntry;
    private EditText PasswordTextEntry;
    private View LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the managed channel
        c = ManagedChannelGetter.get();
        auth = AuthGrpc.newBlockingStub(c);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Initialize Buttons
        SignupButton = findViewById(R.id.SignupButton);

        UsernameTextEntry = findViewById(R.id.UsernameTextEntry);
        PasswordTextEntry = findViewById(R.id.PasswordTextEntry);

        LoginButton = findViewById(R.id.EnterButton);

        //Set up listener
        SignupButton.setOnClickListener(this);
        LoginButton.setOnClickListener(this);
    }


    //Watches Button Clicks
    @Override
    public void onClick(View v){
        //grab id of clicked button
        int id = v.getId();

        //If sign up, go to sign up page
        if (id == R.id.SignupButton){
            startActivity(new Intent(LoginActivity.this , SignupActivity.class));
        }

        if (id == R.id.EnterButton) {
            var result = login(
                    UsernameTextEntry.getText().toString(),
                    PasswordTextEntry.getText().toString());

            if (result.isFailure()) {
                var failure = result.getFailure();

                // Clear the password field
                PasswordTextEntry.getText().clear();

                // Tell the user what the problem was
                switch (result.getFailure().getProblem()) {
                    case InvalidUsernamePassword:
                        UsernameTextEntry.setError("Incorrect username or password");

                    case ServerError:
                    default:
                        UsernameTextEntry.setError("Server Error. Try again later.");
                }
            }
            else if (result.isSuccess()) {
                var sessionToken = result.getSuccess();
                SessionTokenManager.saveSessionToken(sessionToken);

                // Go directly to the homepage
                var toHomePage = new Intent(LoginActivity.this, HomeActivity.class);
                toHomePage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toHomePage);
            }
        }
    }

    // Use a helper function rather than calling it directly in onClick so it's easier to move if needed later on
    private Result<String, Problem> login(String username, String password) {

        // We need to build the request first.
        LoginRequest request = LoginRequest.newBuilder()
                // Set the username to send in the request
                .setUsername(username)

                // Set the password to send in the request
                .setPassword(password)
                .build();

        ds454.locomotive.generated.grpc.LoginResponse response = auth.login(request);

        if (response.hasSessionToken()) {
            return Result.success(response.getSessionToken());
        }
        return Result.failure(response.getProblem());
    }

}