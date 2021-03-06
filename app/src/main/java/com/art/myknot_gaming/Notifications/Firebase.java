//package com.art.myknot_gaming.Notifications;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.util.Log;
//
//import androidx.core.app.NotificationCompat;
//
//import com.art.myknot_gaming.HomeActivity;
//import com.art.myknot_gaming.R;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//
//public class Firebase extends FirebaseMessagingService {private static final String TAG = "MyGcmListenerService";
//
//    @Override
//    public void onMessageReceived(RemoteMessage message) {
//
//        String image = message.getNotification().getIcon();
//        String title = message.getNotification().getTitle();
//        String text = message.getNotification().getBody();
//        String sound = message.getNotification().getSound();
//
//        int id = 0;
//        Object obj = message.getData().get("id");
//        if (obj != null) {
//            id = Integer.valueOf(obj.toString());
//        }
//
//        this.sendNotification(new NotificationData(image, id, title, text, sound));
//    }
//
//    /**
//     * Create and show a simple notification containing the received GCM message.
//     *
//     * @param notificationData GCM message received.
//     */
//    private void sendNotification(NotificationData notificationData) {
//
//        Intent intent = new Intent(this, Notifications.class);
//        intent.putExtra(NotificationData.TEXT, notificationData.getTextMessage());
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder notificationBuilder = null;
//        try {
//
//            notificationBuilder = new NotificationCompat.Builder(this,"High")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(URLDecoder.decode(notificationData.getTitle(), "UTF-8"))
//                    .setContentText(URLDecoder.decode(notificationData.getTextMessage(), "UTF-8"))
//                    .setAutoCancel(true)
//                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                    .setContentIntent(pendingIntent);
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        if (notificationBuilder != null) {
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            assert notificationManager != null;
//            notificationManager.notify(notificationData.getId(), notificationBuilder.build());
//        } else {
//            Log.d(TAG, "notificationBuilder");
//        }
//    }
//}
