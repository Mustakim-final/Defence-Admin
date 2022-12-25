package com.example.doctorapp.SetNotification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.view.View;

public class NotificationExr extends Application {
    public static final String FCM_CHANNEL_ID="FCM_CHANNEL_ID";
    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel fcmChannel=new NotificationChannel(FCM_CHANNEL_ID,"FCM_Channel", NotificationManager.IMPORTANCE_HIGH);
            fcmChannel.enableVibration(false);
            fcmChannel.setLockscreenVisibility(View.VISIBLE);
            fcmChannel.enableLights(true);
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(fcmChannel);
        }
    }
}
