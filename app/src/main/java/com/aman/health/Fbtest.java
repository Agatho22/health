package com.aman.health;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;
import java.util.List;


public class Fbtest extends AppCompatActivity {
    private FirebaseAuth mfirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private TextView textView2, textView4;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbtest);

        textView2 = findViewById(R.id.textView2);
        textView4 = findViewById(R.id.textView4);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        String uid = user != null ? user.getUid() : null; // 로그인한 유저의 고유 uid 가져오기
        Log.e("test","아이 "+uid);


        mfirebaseAuth = mfirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기
        DatabaseReference email = mDatabaseRef.child("Users").child(uid).child("Email");    // 이메일
        DatabaseReference name = mDatabaseRef.child("Users").child(uid).child("Name");
        // uid = 파이어베이스 유저 고유 uid , nickname = 데이터 베이스 child 명








        // 이메일 띄워줌
        name.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String name = snapshot.getValue(String.class);
            textView4.setText(name);
            Log.e("test","나이 "+name);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) { }
    });


    // 이메일 띄워줌
        email.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            String name = snapshot.getValue(String.class);
            textView2.setText(name);
            Log.e("test","이름은 "+name);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) { }
    });







}
}