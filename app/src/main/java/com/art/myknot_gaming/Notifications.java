package com.art.myknot_gaming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.art.myknot_gaming.DashBoard.DashBoard;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Notifications extends AppCompatActivity {
    private BottomNavigationView nav_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

//        nav_bar = findViewById(R.id.home_nav_bar);

        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_home:
                        startActivity(new Intent(Notifications.this, HomeActivity.class));
                    case R.id.navigation_dashboard:
                        startActivity(new Intent(Notifications.this, DashBoard.class));
                        break;
                    case R.id.navigation_notifications:
//                        Toast.makeText(Notifications.this,"Notifications Page", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(Notifications.this, Profile.class));
                        break;


                }

                return false;
            }
        });
    }
}
