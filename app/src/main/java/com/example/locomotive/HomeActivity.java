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
    private DrawerLayout drawerLayout;
    private NavigationView dropDownMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        //Initialize Buttons (and hamburger drawer/drop downs)
        profileButton = findViewById(R.id.profileButton);
        hamburgerButton = findViewById(R.id.hamburgerButton);
        drawerLayout = findViewById(R.id.drawerLayout);
        dropDownMenu = findViewById(R.id.dropDown);

        //On Click Listeners
        profileButton.setOnClickListener(this);
        hamburgerButton.setOnClickListener(this);
        drawerLayout.setOnClickListener(this);
        dropDownMenu.setOnClickListener(this);

        //Drop down menu specific item listener
        dropDownMenu.setNavigationItemSelectedListener(item -> {

            //grab id of item selected
            int picked = item.getItemId();

            if (picked == R.id.dropDownHome){
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
            }
            else if (picked == R.id.dropDownComps){
                startActivity(new Intent(HomeActivity.this, CompsActivity.class));
            }
            else if (picked == R.id.dropDownFriends){
                startActivity(new Intent(HomeActivity.this, FriendsActivity.class));
            } else if (picked == R.id.dropDownProfile){
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }

            //close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    @Override
    public void onClick(View v) {
        //grab id of clicked button
        int id = v.getId();

        //If sign up, go to sign up page
        if (id == R.id.profileButton){
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        }
        else if (id == R.id.hamburgerButton) {

            //open drawer
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
    }
}