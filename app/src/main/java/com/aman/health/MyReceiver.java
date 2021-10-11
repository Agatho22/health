package com.aman.health;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyReceiver extends BroadcastReceiver {

    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private FirebaseDatabase mDatabase;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        int wal = intent.getIntExtra("walk", 250);
        Log.d("만보기 값", "" + wal);
        Intent mServiceintent = new Intent(context, MyService.class);
        context.startService(mServiceintent);
/*
        int walkcount = intent.getIntExtra("walkcount",0);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");


        FirebaseUser user = mfirebaseAuth.getCurrentUser();
        String uid = user.getUid();

        mDatabase.getReference().child("Users").child(uid).setValue(walkcount);
*/
    }

}