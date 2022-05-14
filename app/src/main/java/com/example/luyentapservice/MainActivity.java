package com.example.luyentapservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText mEdtText;
    Button mBtnStart, mBtnStop;
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
        Intent intent=new Intent(this, MyService.class);
        stopService(intent);
    }

    //xu ly nut start
    private void clickStartService() {
        Intent intent=new Intent(this,MyService.class);

        Song song=new Song("Nhac","Dong Nhi",R.drawable.ic_launcher_background,R.raw.baiso01);
        //gui intent thong qua bundle
        Bundle bundle=new Bundle();
        bundle.putSerializable("object_song",song);

        intent.putExtras(bundle);
        startService(intent);
    }

    //anh xa cac item cua layout
    private void initItem() {
        mEdtText=findViewById(R.id.edt_data_intent);
        mBtnStart=findViewById(R.id.btn_start_service);
        mBtnStop=findViewById(R.id.btn_stop_service);
    }
}