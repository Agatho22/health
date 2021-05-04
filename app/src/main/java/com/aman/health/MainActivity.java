package com.aman.health;
import android.content.Intent;
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

    private TextView tv_id, tv_pass;
    private Button btn_water;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_id = findViewById(R.id.tv_id);
        tv_pass = findViewById(R.id.tv_pass);
        btn_water = findViewById(R.id.btn_water);


        btn_water.setOnClickListener(new View.OnClickListener() { //water 버튼 클릭시 water 액티비티로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.aman.health.MainActivity.this, WaterActivity.class);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPass = intent.getStringExtra("userPass");


        tv_id.setText(userID);
        tv_pass.setText(userPass);

    }
}
