package com.example.luyentapservice;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {
    public static final String CHANNEL_ID="My_Channel";
    @Override
    public void onCreate() {
        super.onCreate();
        createChanelNotification();
    }

    private void createChanelNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            //TAO DOI TUONG NOTIFICATION
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,"Channel Service Example", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationChannel.setSound(null,null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
