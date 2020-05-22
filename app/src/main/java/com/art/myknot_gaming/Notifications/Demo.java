package com.art.myknot_gaming.Notifications;

import android.app.Application;

import com.onesignal.OneSignal;

public class Demo extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}

