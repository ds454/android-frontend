package com.example.locomotive;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    //Declare Buttons
    private Button SignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Initialize Buttons
        SignupButton = findViewById(R.id.SignupButton);

        //Set up listener
        SignupButton.setOnClickListener(this);
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
    }
}