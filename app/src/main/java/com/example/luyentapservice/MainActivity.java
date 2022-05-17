package com.example.luyentapservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button mBtnStart, mBtnStop;

    private RelativeLayout relativeLayout;
    private ImageView mImgSong, mImgPlayOrPause, mImgClear;
    private TextView txTitleSong, tvSingleSong;
    private Song mSong;
    private boolean isPlaying;
    private int action;

    //ham se nhan intetnt ben service
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle == null) {
                return;
            }
            mSong = (Song) bundle.getSerializable("object_song");
            isPlaying = bundle.getBoolean("statusPlayer");
            action = bundle.getInt("action_music");

            handleLayout(action);
        }
    };

    private void handleLayout(int action) {
        switch (action) {
            case MyService.ACTION_START:
                relativeLayout.setVisibility(View.VISIBLE);
                showInforSong();
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_PAUSE:
            case MyService.ACTION_RESUME:
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_CLEAR:
                relativeLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void showInforSong()
    {
        if(mSong == null)
        {
            return;
        }



        mImgSong.setImageResource(mSong.getImage());
        txTitleSong.setText(mSong.getTitle());
        tvSingleSong.setText(mSong.getSingle());


        mImgPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying)
                {
                    sendActionToService(MyService.ACTION_PAUSE);
                }
                else
                {
                    sendActionToService(MyService.ACTION_RESUME);
                }
            }
        });

        mImgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendActionToService(MyService.ACTION_CLEAR);
            }
        });
    }

    private void setStatusButtonPlayOrPause()
    {
        if(isPlaying)
        {
            mImgPlayOrPause.setImageResource(R.drawable.icons_stop_);
        }
        else
        {
            mImgPlayOrPause.setImageResource(R.drawable.icons_play);
        }
    }

    private void sendActionToService(int action)
    {
        Intent intent=new Intent(this, MyService.class);
        intent.putExtra("action_music2",action);
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initItem();

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStartService();
            }
        });

        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStopService();
            }
        });



    }

    //xu ly nut stop
    private void clickStopService() {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    //xu ly nut start
    private void clickStartService() {
        Intent intent = new Intent(this, MyService.class);

        Song song = new Song("Nhac", "Dong Nhi", R.drawable.icons_stop_, R.raw.baiso01);

        //gui intent thong qua bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", song);
        intent.putExtras(bundle);

        startService(intent);
    }

    //anh xa cac item cua layout
    private void initItem() {
        mBtnStart = findViewById(R.id.btn_start_service);
        mBtnStop = findViewById(R.id.btn_stop_service);
        relativeLayout = findViewById(R.id.layout_bottom);
        mImgSong = findViewById(R.id.img_song);
        mImgPlayOrPause = findViewById(R.id.imgPlayOrPause);
        mImgClear = findViewById(R.id.imgCancel);
        txTitleSong = findViewById(R.id.txtTitle);
        tvSingleSong = findViewById(R.id.txtSinglrSong);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("send_content_to_activity"));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}