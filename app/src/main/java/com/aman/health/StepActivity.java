package com.aman.health;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StepActivity extends AppCompatActivity implements SensorEventListener {

    TextView tv_sensor;
    SensorManager sm;
    Sensor sensor_step_detector;

    int steps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        tv_sensor = (TextView)findViewById(R.id.sensor);        // 텍스트뷰 인식
        tv_sensor.setText("0"); // 걸음 수 초기화 및 출력
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);   // 센서 매니저 생성
        sensor_step_detector = sm.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);  // 스템 감지 센서 등록

    }

    @Override
    protected void onResume(){
        super.onResume();
        sm.registerListener(this, sensor_step_detector, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    // 센서값이 변할때
    @Override
    public void onSensorChanged(SensorEvent event){

        // 센서 유형이 스텝감지 센서인 경우 걸음수 +1
        switch (event.sensor.getType()){
            case Sensor.TYPE_STEP_DETECTOR:
                tv_sensor.setText("" + (++steps));
                break;
        }
    }
}


