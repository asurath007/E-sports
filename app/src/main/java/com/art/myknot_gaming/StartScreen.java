package com.art.myknot_gaming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.art.myknot_gaming.Login.LogIn;

public class StartScreen extends AppCompatActivity {

    private ImageView ic_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        ic_logo = findViewById(R.id.ic_logo);

        ic_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ic_logo.startAnimation(AnimationUtils.loadAnimation(StartScreen.this, R.anim.splash_out));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ic_logo.setVisibility(View.GONE);
                            startActivity(new Intent(StartScreen.this, LogIn.class));
                            finish();
                        }
                    }, 1000);
                }
            }, 2000);
    }
}
