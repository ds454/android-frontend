package com.example.locomotive;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    private Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        //Initialize Buttons
        LoginButton = findViewById(R.id.LoginButton);

        //Set up listener
        LoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //grab id of clicked button
        int id = v.getId();

        //If sign up, go to sign up page
        if (id == R.id.LoginButton){
            startActivity(new Intent(SignupActivity.this , LoginActivity.class));
        }
    }
}