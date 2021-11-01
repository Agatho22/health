package com.aman.health;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class WaterActivity extends AppCompatActivity {
    private int water, i_water;
    private String s_water;
    private EditText et_water;
    private Button btn_select, btn_clear, btn_wa_memo;
    private TextView wa_info;
    private TextView wa_info22;

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        wa_info22 = findViewById(R.id.wa_info22);
        wa_info = findViewById(R.id.wa_info2);
        btn_select = findViewById(R.id.btn_select);
        btn_clear = findViewById(R.id.btn_clear);
        et_water = (EditText) findViewById(R.id.et_water);
        btn_wa_memo = findViewById(R.id.btn_wa_memo);

        pref = Objects.requireNonNull(getSharedPreferences("pref", Activity.MODE_PRIVATE));
        editor = pref.edit();
        water = pref.getInt("watercount", 0);
        wa_info22.setText("오늘 하루목표 물 섭취까지 남은량 :" + (2000 - water) + "ml");
        wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");


        btn_select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int i_water = Integer.parseInt(et_water.getText().toString());
                if (i_water < 0) {
                    Toast.makeText(getApplicationContext(), "잘못입력하셨습니다. ", Toast.LENGTH_SHORT).show();
                } else if ((water + i_water) > 2000) {
                    Toast.makeText(getApplicationContext(), i_water + "ml추가완료. ", Toast.LENGTH_SHORT).show();
                    water += i_water;
                    editor.putInt("watercount", water);
                    editor.commit(); // 저장
                    wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");
                    wa_info22.setText("오늘 하루 물 목표 섭취까지 남은량 :" + 0 + "ml");
                } else {
                    Toast.makeText(getApplicationContext(), i_water + "ml추가완료. ", Toast.LENGTH_SHORT).show();
                    water += i_water;
                    editor.putInt("watercount", water);
                    editor.commit(); // 저장
                    wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");
                    wa_info22.setText("오늘 하루 물 목표 섭취까지 남은량 :" + (2000 - water) + "ml");
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putInt("watercount", water);
        editor.commit(); // 저장
    }
}