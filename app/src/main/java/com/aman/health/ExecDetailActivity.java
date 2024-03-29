package com.aman.health;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ExecDetailActivity extends AppCompatActivity {

    EditText time_out_min, time_out_sec;
    Button btn_reset;
    InputMethodManager imm;
    ArrayList<Exercise> item;
    ImageView btn_start, imageView;


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String time = getTimeout();
            //0초가 되었을때
            if (time.equals("00:00")) {
                reset(); //타이머 초기화
            } else { //0초가 아닐때
                handler.sendEmptyMessage(0);
            }
        }
    };

    final static int INIT = 0;
    final static int RUN = 1;
    final static int PAUSE = 2;

    int Pstatus = INIT;

    long baseTime;
    long pauseTime;
    long setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exec_detail);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        time_out_min = findViewById(R.id.time_out_min);
        //time_out_min의 텍스트가 변할때 이벤트 발생
        time_out_min.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (time_out_min.hasFocus() && getEditTime() != 0) { //EditText에 입력된 시간이 0초 이상일 경우 미리 시간 세팅
                    setTime();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        time_out_sec = findViewById(R.id.time_out_sec);
        //time_out_sec의 텍스트가 변할때 이벤트 발생
        time_out_sec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (time_out_sec.hasFocus() && getEditTime() != 0) { //EditText에 입력된 시간이 0초 이상일 경우 미리 시간 세팅
                    setTime();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_start = findViewById(R.id.btn_start);
        btn_reset = findViewById(R.id.btn_reset);
        //시작버튼 이벤트
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getEditTime() != 0) { //시간이 0이 아닐 경우
                    hideKeyboard(); //키보드 숨기기
                    start(Pstatus);
                } else { //시간이 0일 경우
                    Toast.makeText(ExecDetailActivity.this, "시간을 입력하세요", Toast.LENGTH_SHORT).show();
                    time_out_min.requestFocus();
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        Intent intent = getIntent();
        Exercise save = new Exercise();

        save.setName(intent.getExtras().getString("getName"));
        save.setText(intent.getExtras().getString("getText"));
        save.setImage(intent.getExtras().getInt("getImage"));


        imageView = findViewById(R.id.image2);

        imageView.setImageResource(save.getImage());

        Button backbtn = (Button) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExecDetailActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void reset() {
        handler.removeCallbacksAndMessages(null); //핸들러 메시지 전달 종료
        Pstatus = INIT; //상태 변수 초기화
        setTime = 0; //long 형으로 변환할 변후 초기화
        time_out_min.setText("00");
        time_out_min.setEnabled(true);
        time_out_sec.setText("00");
        time_out_sec.setEnabled(true);
        btn_reset.setEnabled(false); //초기화 버튼 비활성화
    }

    public void hideKeyboard() { //키보드 숨기기
        imm.hideSoftInputFromWindow(time_out_min.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(time_out_sec.getWindowToken(), 0);
    }

    public long getEditTime() {
        long min = Long.parseLong(time_out_min.getText().toString()) * 1000 * 60;
        long sec = Long.parseLong(time_out_sec.getText().toString()) * 1000;
        return min + sec;
    }

    public void start(int status) {
        switch (status) {
            case INIT: //시작
                baseTime = SystemClock.elapsedRealtime();
                btn_start.setImageResource(R.drawable.play);
                btn_reset.setEnabled(false);
                time_out_min.setEnabled(false);
                time_out_sec.setEnabled(false);
                handler.sendEmptyMessage(0); //핸들러로 메시지를 전달하여 타이머를 시작
                Pstatus = RUN;
                break;
            case RUN: //멈춤
                handler.removeMessages(0); //핸들러에서 메세지 삭제
                pauseTime = SystemClock.elapsedRealtime(); //멈출 시간 기록
                btn_start.setImageResource(R.drawable.pause2);
                btn_reset.setEnabled(true);
                Pstatus = PAUSE;
                break;
            case PAUSE: //재시작
                long now = SystemClock.elapsedRealtime(); //현재 시간 다시 기록
                baseTime += (now - pauseTime); //타이머 시간 세팅
                btn_start.setImageResource(R.drawable.play);
                btn_reset.setEnabled(false);
                handler.sendEmptyMessage(0); //타이머 재시작
                Pstatus = RUN;
                break;
        }
    }

    public String getTimeout() { //핸들러 안에서 EditText 의 시간을 return 해주고 프로그레스를 세팅
        long now = SystemClock.elapsedRealtime();
        long outTime = baseTime - now + setTime;
        long sec = outTime / 1000 % 60;
        long min = outTime / 1000 / 60;

        if (outTime % 1000 / 10 != 0 && sec < 60) {
            sec += 1;
            if (sec == 60) {
                sec = 0;
                min += 1;
            }
        }

        @SuppressLint("DefaultLocale")
        String easy_outTime = String.format("%02d:%02d", min, sec);
        String[] times = easy_outTime.split(":");

        time_out_min.setText(times[0]);
        time_out_sec.setText(times[1]);

        return easy_outTime;
    }

    public void setTime() { //프로그레스바 최대치 세팅
        setTime = Long.parseLong(time_out_min.getText().toString()) * 1000 * 60 +
                Long.parseLong(time_out_sec.getText().toString()) * 1000;
    }

}