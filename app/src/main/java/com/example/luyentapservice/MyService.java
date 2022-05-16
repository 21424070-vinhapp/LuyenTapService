package com.example.luyentapservice;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompatSideChannelService;

public class MyService extends Service {

    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_CLEAR = 3;
    private boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private Song mSong;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //nhan du lieu string tu intent
        //String getData=intent.getStringExtra("data_send");

        //nhan object tu bundle Main
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Song song = (Song) bundle.get("object_song");
            mSong = song;
            startMusic(song);
            sendNotification(song);
        }

        //Log.d("BBB", "onStartCommand: "+mSong);
        //Log.d("BBB", "onStartCommand: ");

        //nhan action nhan tu broadcast receiver
        if(intent.getIntExtra("action_music_service", 0)!=0)
        {
            int actionMusic = intent.getIntExtra("action_music_service", 0);
            Log.d("BBB", "onStartCommand: " + actionMusic);
            handleActionMusic(actionMusic);
        }



        //Log.d("BBB", "onStartCommand: "+actionMusic);

        return START_NOT_STICKY;
    }

    private void sendNotification(Song song) {
        //gan bitmap voi getImage cua song
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), song.getImage());

        Intent intent = new Intent(this, MainActivity.class);
        //xac dinh noi man hinh tra ve khi nhan vao notification
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //custom notification su dung remoteviews de gan cac doi tuong cu the tren view
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);

        remoteViews.setTextViewText(R.id.txtTitle, song.getTitle());
        remoteViews.setTextViewText(R.id.txtSinglrSong, song.getSingle());
        remoteViews.setImageViewBitmap(R.id.img_song, bitmap);
        remoteViews.setImageViewResource(R.id.imgPlayOrPause, R.drawable.icons_stop_);

        //xu ly su kien khi click vao 2 item playorpause va cancel tren custom_notification
        //va thay doi hinh anh khi click giua 2 nut play vs pause
        if (isPlaying) {
            remoteViews.setOnClickPendingIntent(R.id.imgPlayOrPause, getPendingAction(this, ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.imgPlayOrPause, R.drawable.icons_stop_);
        } else {
            remoteViews.setOnClickPendingIntent(R.id.imgPlayOrPause, getPendingAction(this, ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.imgPlayOrPause, R.drawable.icons_play);
        }

        remoteViews.setOnClickPendingIntent(R.id.imgCancel, getPendingAction(this, ACTION_CLEAR));

        //tao notofication
        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();

        //start forceground
        startForeground(1, notification);
    }

    //ham nhan du lieu tra ve pendingIntent tren custom_notification
    //gom co bien moi truong context la custom_notification
    //action la trang thai cua cac nut tren notification play, pause hoac cancel
    //ta co 3 hanh dong tuong ung 3 action => su dung broadcast receiver de nhan du lieu intent gui qua
    private PendingIntent getPendingAction(Context context, int action) {

        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.putExtra("action_music", action);

        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void handleActionMusic(int action) {
        switch (action) {
            case ACTION_PAUSE:
                onPauseMusic();
                break;
            case ACTION_RESUME:
                onResumeMusic();
                break;
            case ACTION_CLEAR:
                stopSelf();
                break;
        }
    }


    //trinh phat nhac
    private void startMusic(Song song) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), song.getResource());
        }
        mediaPlayer.start();
        isPlaying = true;
    }


    //xu ly nut pause
    private void onPauseMusic() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            sendNotification(mSong);
        }
    }

    //xu ly nut play
    private void onResumeMusic() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(mSong);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BBB", "onCreate: MyService");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        Log.d("BBB", "onDestroy: MyService");
    }
}
