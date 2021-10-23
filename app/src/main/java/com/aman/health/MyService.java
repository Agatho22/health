package com.aman.health;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MyService extends Service {
    protected Context context;
    private int wal;
    private static SharedPreferences pref;
    protected static SharedPreferences.Editor editor;
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스

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
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = simpleDate.format(mDate);


        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        wal = pref.getInt("walk", 250);
        editor.clear();
        editor.commit();

        Log.d("서비스 만보기 값", "" + wal);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseRef.child(uid).child("walkcount").child(getTime).setValue(wal);

        return super.onStartCommand(intent, flags, startId);
    }

}