package com.art.myknot_gaming.Notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.art.myknot_gaming.DashBoard.DashBoard;
import com.art.myknot_gaming.HomeActivity;
import com.art.myknot_gaming.Profile;
import com.art.myknot_gaming.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Notifications extends AppCompatActivity {
    private BottomNavigationView nav_bar;
    private EditText tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        tv = findViewById(R.id.not_tv);
        nav_bar = findViewById(R.id.not_nav_bar);
        nav_bar.setSelectedItemId(R.id.navigation_notifications);
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
        SharedPreferences sp = getSharedPreferences("msgKey",MODE_PRIVATE);
        tv.setText(sp.getString("value",""));


    }
}
