package com.aman.health;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {

    int timerTime = 60;
    TimerHandler timer;
    Button btn_start, btn_reset;
    TextView textview4;
    boolean isRunnig = true;
    int status = 0;

    int pStatus = 0;
    private Handler handler = new Handler();
    TextView tv;

    public void onStartPauseButton(View view) {
        if(status == 0)
        {
            status=1;
            timer.sendEmptyMessage(0);
            btn_start.setText("일시정지");
        }
        else if(status == 1)
        {
            status=0;
            timer.sendEmptyMessage(1);
            btn_start.setText("시작");
        }
    }

    public void onStopButton(View view) {
        status = 0;
        timer.sendEmptyMessage(2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        textview4 = (TextView) findViewById(R.id.textview4);
        timer = new TimerHandler();

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);

        /* ObjectAnimator animation = ObjectAnimator.ofInt(mProgress, "progress", 0, 100);
        animation.setDuration(50000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start(); */

        tv = (TextView) findViewById(R.id.tv);
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (pStatus < 100) {
                    pStatus += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress.setProgress(pStatus);
                            tv.setText(pStatus + "%");

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(600); //thread will take approx 1.5 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer != null)
            timer.removeMessages(0);
        isRunnig = false;
    }

    class TimerHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    if (timerTime == 0) {
                        textview4.setText("운동시간: " + timerTime);
                        removeMessages(0);
                        break;
                    }
                    textview4.setText("운동시간: " + timerTime--);
                    sendEmptyMessageDelayed(0, 1000);
                    Log.d("test", "msg.what:0 time =" + timerTime);
                    break;

                case 1:
                    removeMessages(0);
                    textview4.setText("운동시간: " + timerTime);
                    Log.d("test", "msg.what:1 time =" + timerTime);
                    break;

                case 2:
                    removeMessages(0);
                    timerTime = 60;
                    textview4.setText("운동시간: " + timerTime);
                    btn_start.setText("시작");
                    Log.d("test", "msg.what:2 time =" + timerTime);
                    break;
            }
        }
    }
}