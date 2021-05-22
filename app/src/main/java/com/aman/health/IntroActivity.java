package com.aman.health;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //타이틀바 숨김
        //getSupportActionBar().hide();

        //2초 후 인트로화면 제거
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, IntroActivity.class);
                startActivity(intent);  //다음화면으로 넘어가기

                //뒤로가기버튼을 통하여 Intro Screen을 오지못하도록 종료
                finish();
            }

        }, 2000);
    }
}
