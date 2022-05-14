package com.example.luyentapservice;

import static com.example.luyentapservice.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //nhan du lieu string tu intent
        String getData=intent.getStringExtra("data_send");
        sendNotification(getData);
        Log.d("BBB", "onStartCommand: ");
       return START_NOT_STICKY;
    }

    private void sendNotification(String getData) {
        Intent intent=new Intent(this,MainActivity.class);
        //xac dinh noi man hinh tra ve khi nhan vao notification
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Tile example")
                .setContentText(getData)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentIntent(pendingIntent)
                .build();

        //start forceground
        startForeground(1,notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BBB", "onCreate: MyService");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BBB", "onDestroy: MyService");
    }
}
