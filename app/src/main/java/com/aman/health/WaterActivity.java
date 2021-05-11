package com.aman.health;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class WaterActivity extends AppCompatActivity {
    private int water,i_water;
    private String s_water;
    private EditText et_water;
    private Button btn_select, btn_clear ,btn_wa_memo;
    private TextView wa_info;
    private TextView wa_info22;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        wa_info22 = findViewById(R.id.wa_info22);
        wa_info = findViewById(R.id.wa_info2);
        btn_select = findViewById(R.id.btn_select);
        btn_clear = findViewById(R.id.btn_clear);
        et_water =(EditText)findViewById(R.id.et_water);
        btn_wa_memo = findViewById(R.id.btn_wa_memo);
        btn_select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int i_water = Integer.parseInt(et_water.getText().toString());
                if(i_water < 0 ){
                    Toast.makeText(getApplicationContext(),"잘못입력하셨습니다. ",Toast.LENGTH_SHORT).show();
                }else if((water+i_water) > 2000){
                    Toast.makeText(getApplicationContext(),i_water +"ml추가완료. ",Toast.LENGTH_SHORT).show();
                    water += i_water;
                    wa_info.setText("오늘 하루 물 섭취량 :"+ water+"ml");
                    wa_info22.setText("오늘 하루 물 목표 섭취까지 남은량 :"+0+"ml");
                }else{
                    Toast.makeText(getApplicationContext(),i_water +"ml추가완료. ",Toast.LENGTH_SHORT).show();
                    water += i_water;
                    wa_info.setText("오늘 하루 물 섭취량 :"+ water+"ml");
                    wa_info22.setText("오늘 하루 물 목표 섭취까지 남은량 :"+ (2000-water)+"ml");
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i_water = Integer.parseInt(et_water.getText().toString());
                et_water.setText("");
                wa_info.setText("오늘 하루 물 섭취량 :"+0+"ml");
                wa_info22.setText("오늘 하루 물 목표 섭취까지 남은량 :"+2000+"ml");
                i_water=0;
                water = i_water;
            }
        });
        btn_wa_memo.setOnClickListener(new View.OnClickListener() { //water 버튼 클릭시 water 액티비티로 이동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.aman.health.WaterActivity.this, wa_memoActivity.class);
                startActivity(intent);
            }
        });
    }
}