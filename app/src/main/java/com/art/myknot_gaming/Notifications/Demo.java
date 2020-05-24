package com.art.myknot_gaming.Notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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
    public Demo(Context context) {
        this.context = context;
    }


    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;

        String title = result.notification.payload.title;
        String body = result.notification.payload.body;


        sp = context.getSharedPreferences("loginKey", MODE_PRIVATE);
        String id = sp.getString("LID","");
//        db.collection("Users").document(id).update("Notifications", )
        SharedPreferences sharedPref = context.getSharedPreferences("pushTitle", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("title", title);
        editor.apply();
        SharedPreferences sharedPref1 = context.getSharedPreferences("pushBody", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPref1.edit();
        editor1.putString("body", body);
        editor1.apply();

         Intent intent = new Intent(context, Notifications.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
         intent.putExtra("push_title",title);
         intent.putExtra("push_body",body);
         context.startActivity(intent);
    }
}

