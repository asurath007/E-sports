package com.art.myknot_gaming.DashBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

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
import com.art.myknot_gaming.databinding.ActivityDashBoardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class DashBoard extends AppCompatActivity {
    private BottomNavigationView nav_bar;
    private TabLayout dash_tab;
    private ViewPager dash_view;
    private Button btn;
    ActivityDashBoardBinding binding;
    private String[] titles = new String[]{"Upcoming","Completed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nav_bar = findViewById(R.id.dash_nav_bar);

        dash_tab = findViewById(R.id.dash_tab);
        dash_view = findViewById(R.id.dash_view);
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
//        btn = findViewById(R.id.dash_btn);

//        dash_tab.setupWithViewPager(dash_view);
        /**replace with view-pager*/
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DashBoard.this, AfterPayment.class));
//            }
//        });
}
    private void init() {
        binding.dashNavBar.setSelectedItemId(R.id.navigation_dashboard);
        binding.dashNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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
                        startActivity(new Intent(DashBoard.this, Notifications.class));finish();
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(DashBoard.this, Profile.class));finish();
                        break;
                }
                return false;
            }
        });
        binding.dashView.setAdapter(new ViewPagerFragmentAdapter(this));
        new TabLayoutMediator(binding.dashTab,binding.dashView,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab dash_tab, int position) {
                        dash_tab.setText(titles[position]);
                    }
                }).attach();
    }

    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new UpcomingFragment();
                case 1:
                    return new CompletedFragment();
            }
            return new UpcomingFragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }
}
