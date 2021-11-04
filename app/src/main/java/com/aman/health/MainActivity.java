package com.aman.health;


import static com.aman.health.HomeActivity.mContext;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

    private Button btn_water, btn_profile, btn_timer, btn_logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

//        FbData.resetAlarm(getApplicationContext(), MyReceiver.class, FbData.Alarm, 0, AlarmManager.INTERVAL_DAY);
//        FbData.resetAlarm(getApplicationContext(), MyReceiver.class, FbData.wtdayaction, 8, AlarmManager.INTERVAL_DAY);
        FbData.resetAlarm(getApplicationContext(), MyReceiver.class, FbData.wtweekaction, 0, 1000);



        btn_water = findViewById(R.id.btn_water);
        btn_profile = findViewById(R.id.btn_profile);
        btn_timer = findViewById(R.id.btn_timer);
        btn_logout = findViewById(R.id.btn_logout);


        btn_water.setOnClickListener(new View.OnClickListener() { //water 버튼 클릭시 water 액티비티로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WaterActivity.class);
                startActivity(intent);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() { //걸어 버튼 클릭시 profile 액티비티로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btn_timer.setOnClickListener(new View.OnClickListener() { //타이머 버튼 클릭시 Timer 액티비티로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExecDetailActivity.class);
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

    @SuppressLint("ObsoleteSdkInt")
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  //SDK_INT 버전에서 조건에 의해 차단
            CharSequence name = "MyChannel"; //채널이름
            String description = "channel_description"; //채널설명
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel_Id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}