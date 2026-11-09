package com.example.locomotive;

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
 */

public class LoginActivity extends AppCompatActivity implements View.onClickListener{

    //Declare Buttons
    private Button SignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Initialize Buttons
        SignupButton = findViewById(R.id.SignupButton);


        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
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