package com.art.myknot_gaming.Notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.art.myknot_gaming.Login.LogIn;
import com.google.firebase.firestore.FirebaseFirestore;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class Demo implements OneSignal.NotificationOpenedHandler {

    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreferences sp;
    private String title,body;
    public Demo(Context context) {
        this.context = context;
    }


    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;

        title = result.notification.payload.title;
        body = result.notification.payload.body;

        //open notification page if user is logged in else give login prompt
        sp = context.getSharedPreferences("login", MODE_PRIVATE);
        //check if previously logged
        if (sp.getBoolean("logged", false)) {
            skipLogin();
        }else {
            Intent intent = new Intent(context, LogIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
//        db.collection("Users").document(id).update("Notifications", )
        SharedPreferences sharedPref = context.getSharedPreferences("pushTitle", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("title", title);
        editor.apply();
        SharedPreferences sharedPref1 = context.getSharedPreferences("pushBody", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPref1.edit();
        editor1.putString("body", body);
        editor1.apply();
    }

    private void skipLogin() {
        Intent intent = new Intent(context, Notifications.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("push_title",title);
        intent.putExtra("push_body",body);
        context.startActivity(intent);
    }
}

