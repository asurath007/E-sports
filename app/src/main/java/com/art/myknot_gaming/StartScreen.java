package com.art.myknot_gaming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.art.myknot_gaming.Login.LogIn;
import com.art.myknot_gaming.Notifications.Demo;
import com.onesignal.OneSignal;

public class StartScreen extends AppCompatActivity {

    private ImageView ic_logo;
    private String TAG= "Start Screen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        ic_logo = findViewById(R.id.ic_logo);
        ic_logo.setVisibility(View.GONE);



                    ic_logo.startAnimation(AnimationUtils.loadAnimation(StartScreen.this, R.anim.splash_in));
                    ic_logo.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ic_logo.startAnimation(AnimationUtils.loadAnimation(StartScreen.this, R.anim.splash_out));
                            ic_logo.setVisibility(View.GONE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(StartScreen.this, LogIn.class));
                                    finish();
                                }
                            },500);
                        }
                    }, 1000);

            //one-signal notification handler
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new Demo(this))
                .autoPromptLocation(true)
                .init();

    }
}
