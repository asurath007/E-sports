package com.art.myknot_gaming.DashBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.art.myknot_gaming.AfterPayment.AfterPayment;
import com.art.myknot_gaming.HomeActivity;
import com.art.myknot_gaming.Notifications.Notifications;
import com.art.myknot_gaming.Profile;
import com.art.myknot_gaming.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class DashBoard extends AppCompatActivity {
    private BottomNavigationView nav_bar;
    private TabLayout dash_tab;
    private ViewPager dash_view;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        nav_bar = findViewById(R.id.dash_nav_bar);
//        dash_tab = findViewById(R.id.dashboard_table_layout);
//        dash_view = findViewById(R.id.dashboard_viewPager);
        nav_bar.setSelectedItemId(R.id.navigation_dashboard);
        btn = findViewById(R.id.dash_btn);

//        dash_tab.setupWithViewPager(dash_view);
        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Menu menu = nav_bar.getMenu();
//                MenuItem menuItem = menu.getItem(item.getItemId());
//                menuItem.isChecked() = true;

                switch(item.getItemId()){
                    case R.id.navigation_home:
                        startActivity(new Intent(DashBoard.this, HomeActivity.class));finish();
                        break;
                    case R.id.navigation_dashboard:
//                        Toast.makeText(DashBoard.this,"Dashboard", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_notifications:
                        startActivity(new Intent(DashBoard.this, Notifications.class));finish();
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(DashBoard.this, Profile.class));finish();
                        break;
                }
                return false;
            }
        });

        //replace with view-pager
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoard.this, AfterPayment.class));
            }
        });

}}
