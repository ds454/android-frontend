package com.example.locomotive;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

/*
    Home Activity:

    Screen Click Through:
    - Traveled to via Login or Sign Up page
    - Can go to:
        - Competitions page (via hamburger button and TODO perhaps link on screen feed?)
        - User profile (via hamburger button and icon button)
        - Friends page (via hamburger button)

    Functionality:
    - Display feed with information about competitions:
        - TODO: could be things like acheivement progress from self or friends,
           wins, losses, invites, etc.?

 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    //Image buttons (profile & hamburger menu stuff)
    private ImageButton profileButton;
    private ImageButton hamburgerButton;
    private DrawerLayout drawer;
    private NavigationView dropDownMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);




    }

    @Override
    public void onClick(View v) {
        //grab id of clicked button
        int id = v.getId();

        /*If sign up, go to sign up page
        if (id == R.id.LoginButton){
            startActivity(new Intent(HomeActivity.this , LoginActivity.class));
        }*/
    }

}