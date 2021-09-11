package com.aman.health;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class StepActivity extends Fragment implements SensorEventListener{

    //센서기능 변수 시작
    private long lastTime;
    private float speed, lastX, lastY, lastZ, x, y, z;
    private static final int SHAKE_THRESHOLD = 800;    //속도가 800

    public static final int DATA_X = SensorManager.DATA_X;
    public static final int DATA_Y = SensorManager.DATA_Y;
    public static final int DATA_Z = SensorManager.DATA_Z;

    public SensorManager sensorManager;
    public Sensor accelerormeterSensor;

    public int sensitive=150;
    //센서기능 변수 끝


    //만보계 변수 시작
    public static int cnt = 0;
    public int resultcnt=0;

    public TextView walkcnt;

    //만보계 변수 끝

    public StepActivity() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_step, container, false);


        walkcnt = (TextView)view.findViewById(R.id.walkcnt);
        walkcnt.setText("" + cnt);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }


    @Override
    public void onStart() {
        super.onStart();

        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);

    }


    @Override
    public void onStop() {
        super.onStop();

        if (sensorManager != null)
            sensorManager.unregisterListener(this);

    }


    //sensoreventlistener에 필수. 센서가 변하면 발생하는 함수. 흔들림 감지
    @Override
    public void onSensorChanged(android.hardware.SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //센서종류가 가속도센서일때
            long currentTime = System.currentTimeMillis();
            //현재시간을 가져옴
            long gabOfTime = (currentTime - lastTime);
            //현재시간-측정시간

            if (gabOfTime > sensitive) {
                //현재시간-측정시간이 0.14초 이상되었을때, 흔들림 감지
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    //속도가 800이상일 때 흔들림을 감지한다.
                    // 이벤트발생!!

                    resultcnt = (++cnt);

                    walkcnt.setText(""+resultcnt);




                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }

        }

    }

    //sensoreventlistener에 필수
    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {
    }
}