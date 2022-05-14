package com.example.luyentapservice;

import static com.example.luyentapservice.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
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

public class MyService extends Service {

    public static final int ACTION_STOP=0;
    public static final int ACTION_PAUSE=1;
    public static final int ACTION_PLAY=2;
    boolean isPlaying;
    private MediaPlayer mediaPlayer;
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
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            Song song= (Song) bundle.get("object_song");
            startMusic(song);
            sendNotification(song);
        }

        Log.d("BBB", "onStartCommand: ");
       return START_NOT_STICKY;
    }
    //trinh phat nhac
    private void startMusic(Song song) {
        if(mediaPlayer==null)
        {
            mediaPlayer=MediaPlayer.create(getApplicationContext(),song.getResource());
        }
        mediaPlayer.start();
        isPlaying=true;
    }

    private void sendNotification(Song song) {
        //gan bitmap voi getImage cua song
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),song.getImage());

        Intent intent=new Intent(this,MainActivity.class);
        //xac dinh noi man hinh tra ve khi nhan vao notification
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //custom notification su dung remoteviews de gan cac doi tuong cu the tren view
        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.layout_custom_notification);

        remoteViews.setTextViewText(R.id.txtTitle,song.getTitle());
        remoteViews.setTextViewText(R.id.txtSinglrSong,song.getSingle());
        remoteViews.setImageViewBitmap(R.id.img_song,bitmap);
        remoteViews.setImageViewResource(R.id.imgPause,R.drawable.icons_stop_);

        //tao notofication
        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();

        //start forceground
        startForeground(1,notification);
    }

    private void handleActionMusic(int action)
    {
        switch (action)
        {
            case ACTION_PAUSE:
                onPauseMusic();
                break;
            case ACTION_STOP:
                stopSelf();
                break;
            case ACTION_PLAY:
                onPlayMusic();
                break;
        }
    }

    private void onPauseMusic() {
        if(mediaPlayer!=null && isPlaying)
        {
            mediaPlayer.pause();
            isPlaying=false;
        }
    }

    private void onPlayMusic() {
        if(mediaPlayer!=null && !isPlaying)
        {
            mediaPlayer.start();
            isPlaying=true;
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
        if(mediaPlayer!=null)
        {
            mediaPlayer.release();
            mediaPlayer=null;
        }
        Log.d("BBB", "onDestroy: MyService");
    }
}
