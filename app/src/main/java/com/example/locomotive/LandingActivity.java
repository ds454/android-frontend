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

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

    //Declare buttons
    private Button LoginButton;
    private Button SignupButton;



    //On start
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);

        //Initialize Buttons
        LoginButton = findViewById(R.id.LoginButton);
        SignupButton = findViewById(R.id.SignupButton);


        //Set Up Listener
        LoginButton.setOnClickListener(this);
        SignupButton.setOnClickListener(this);

        //old code
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.landing), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }

    //On Click Listener (watch button clicks, take actions accordingly)
    @Override
    public void onClick(View v) {
        //grab id of clicked button
        int id = v.getId();

        //If login, go to login page
        if (id == R.id.LoginButton) {
            startActivity(new Intent(LandingActivity.this, LoginActivity.class));
        }
        //If sign up, go to sign up page
        else if (id == R.id.SignupButton) {
            startActivity(new Intent(LandingActivity.this, SignupActivity.class));
        }


    }
}