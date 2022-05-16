package com.example.luyentapservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int actionMusic=intent.getIntExtra("action_music",0);
        //Log.d("BBB", "onReceive: "+actionMusic);

        Intent intentService=new Intent(context,MyService.class);
        intentService.putExtra("action_music2",actionMusic);

        context.startService(intentService);
    }
}
