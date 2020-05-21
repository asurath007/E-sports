package com.art.myknot_gaming.DashBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.art.myknot_gaming.HomeActivity;
import com.art.myknot_gaming.Profile;
import com.art.myknot_gaming.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class DashBoard extends AppCompatActivity {
    private BottomNavigationView nav_bar;
    private TabLayout dash_tab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        nav_bar = findViewById(R.id.dash_nav_bar);
        dash_tab = findViewById(R.id.dashboard_table_layout);
        ViewPager dash_view = findViewById(R.id.dashboard_viewPager);


        dash_tab.setupWithViewPager(dash_view);
        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_home:
                        startActivity(new Intent(DashBoard.this, HomeActivity.class));finish();
                        break;
                    case R.id.navigation_dashboard:
//                        Toast.makeText(DashBoard.this,"Dashboard", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_notifications:
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(DashBoard.this, Profile.class));finish();
                        break;
                }
                return false;
            }
        });

    }
}
