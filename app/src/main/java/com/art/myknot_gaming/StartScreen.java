package com.art.myknot_gaming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.art.myknot_gaming.Login.LogIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.onesignal.OneSignal;

public class StartScreen extends AppCompatActivity {

    private ImageView ic_logo;
    private String TAG= "Start Screen";


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
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
//        // Get token
//        // [START retrieve_current_token]
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//
//                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        SharedPreferences sharedPref = getSharedPreferences("msgKey", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPref.edit();
//                        editor.putString("value", msg);
//                        editor.apply();
//                        Toast.makeText(StartScreen.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
//        // [END retrieve_current_token]
    }
}
