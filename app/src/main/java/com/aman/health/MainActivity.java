package com.aman.health;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_water, btn_step, btn_timer, btn_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_water = findViewById(R.id.btn_water);
        btn_step = findViewById(R.id.btn_step);
        btn_timer = findViewById(R.id.btn_timer);
        btn_logout = findViewById(R.id.btn_logout);


        btn_water.setOnClickListener(new View.OnClickListener() { //water 버튼 클릭시 water 액티비티로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.aman.health.MainActivity.this, WaterActivity.class);
                startActivity(intent);
            }
        });

        btn_step.setOnClickListener(new View.OnClickListener() { //걸어 버튼 클릭시 step 액티비티로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.aman.health.MainActivity.this, StepActivity.class);
                startActivity(intent);
            }
        });

        btn_timer.setOnClickListener(new View.OnClickListener() { //타이머 버튼 클릭시 Timer 액티비티로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.aman.health.MainActivity.this, TimerActivity.class);
                startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
                Intent intent = new Intent(MainActivity.this, LoginActivityF.class);
                startActivity(intent);
                SharedPreferences appData = getSharedPreferences("appData", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = appData.edit();

                //editor.clear()는 appData 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                finish();
            }
        });



    }
}
