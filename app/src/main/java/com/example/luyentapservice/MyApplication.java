package com.example.luyentapservice;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {
public static final String CHANNEL_ID="channel_service_example";
    @Override
    public void onCreate() {
        super.onCreate();

        createApplicationChanel();
    }

    private void createApplicationChanel() {
        //KIEM TRA VERSION CUA MAY, O LA API 26
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            //TAO RA DOI TUONG NOTIFICATIONCHANNEL
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID, "My Channel example",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
