package com.aman.health;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProFileFragment extends Fragment {

    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private TextView info_email, info_name, info_age, info_height, info_weight;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        info_email = view.findViewById(R.id.info_email);
        info_name = view.findViewById(R.id.info_name);
        info_age = view.findViewById(R.id.info_age);
        info_height = view.findViewById(R.id.info_height);
        info_weight = view.findViewById(R.id.info_weight);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기
        Log.e("test","아이 "+uid);


        mfirebaseAuth = mfirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기
        DatabaseReference email = mDatabaseRef.child("Users").child(uid).child("Email");    // 이메일
        DatabaseReference name = mDatabaseRef.child("Users").child(uid).child("Name");
        DatabaseReference age = mDatabaseRef.child("Users").child(uid).child("Age");
        DatabaseReference height = mDatabaseRef.child("Users").child(uid).child("Height");
        DatabaseReference weight = mDatabaseRef.child("Users").child(uid).child("Weight");

        // uid = 파이어베이스 유저 고유 uid , nickname = 데이터 베이스 child 명



        // 이메일 띄워줌
        email.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                info_email.setText(value);
                Log.e("test","나이 "+name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        // 이메일 띄워줌
        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                info_name.setText(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        age.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                info_age.setText(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        height.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                info_height.setText(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        weight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                info_weight.setText(value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        return view;
    }
}
