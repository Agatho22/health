package com.aman.health;

import static com.aman.health.FbData.mDatabaseRefU;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MyService extends Service {
    protected Context context;
    private int wal, watco;
    private static SharedPreferences pref;
    protected static SharedPreferences.Editor editor;

    public MyService() {

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
        String getTime = simpleDate.format(mDate);


        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        wal = pref.getInt("walk", 250);
        watco = pref.getInt("watercount", 5000);
        editor.clear();
        editor.commit();
        Log.d("서비스 만보기 값", "" + wal);
        Log.d("서비스 물섭취 값", "" + watco);


        mDatabaseRefU.child("walkcount").child(getTime).setValue(wal);
        mDatabaseRefU.child("watercount").child(getTime).setValue(watco);


        return super.onStartCommand(intent, flags, startId);
    }

}