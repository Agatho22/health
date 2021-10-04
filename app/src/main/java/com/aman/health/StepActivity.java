package com.aman.health;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class StepActivity extends Fragment   implements SensorEventListener {

    //센서기능 변수 시작
    private long lastTime;
    private float speed, lastX, lastY, lastZ, x, y, z;
    private static final int SHAKE_THRESHOLD = 400;    //속도가 400

    public static final int DATA_X = SensorManager.DATA_X;
    public static final int DATA_Y = SensorManager.DATA_Y;
    public static final int DATA_Z = SensorManager.DATA_Z;

    public SensorManager sensorManager;
    public Sensor accelerormeterSensor;

    public int sensitive=400;

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    //센서기능 변수 끝


    //만보계 변수 시작
    public static int resultcnt=0;

    public TextView walkcnt;

    AlarmManager resetAlarmManager;
    PendingIntent resetSender;
    BroadcastReceiver br;


    //만보계 변수 끝

    public StepActivity() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_step, container, false);



        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        resultcnt = pref.getInt("walkcount", 0);
        walkcnt = (TextView)view.findViewById(R.id.walkcnt);
        walkcnt.setText("" + resultcnt);

        resetAlarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(getActivity(),MyReceiver.class);


        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        editor.clear();
        editor.commit();
        resetIntent.putExtra("walkcount",resultcnt);
        resetSender = PendingIntent.getBroadcast(getActivity(), 0, resetIntent, 0);
        // 자정 시간
        Calendar resetCal = Calendar.getInstance();
        //resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY, 03);
        resetCal.set(Calendar.MINUTE,00);
        resetCal.set(Calendar.SECOND, 0);

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
        resetAlarmManager.set(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis(), resetSender);

        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
        String setResetTime = format.format(new Date(resetCal.getTimeInMillis()));

        Log.d("알람 리셋", "ResetHour : " + setResetTime);
        Log.d("알람메소드", "실행됌~~~~~~~~~~~~~~~~~~");
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
        Log.d("걸음 수", "" + resultcnt);

        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_UI);

    }



    @Override
    public void onStop() {
        super.onStop();

        editor.putInt("walkcount", resultcnt);
        editor.apply();

        if (sensorManager != null)
            sensorManager.unregisterListener(this);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        resetAlarm(getActivity());
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
                //현재시간-측정시간이 0.4초 이상되었을때, 흔들림 감지
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    //속도가 400이상일 때 흔들림을 감지한다.
                    // 이벤트발생!!

                    resultcnt = (++resultcnt);

                    walkcnt.setText(""+resultcnt);




                }

                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }

        }

        

    }

    public void resetAlarm(Context context){

    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        editor.putInt("walkcount", resultcnt);
        editor.apply(); // 저장
    }

    //sensoreventlistener에 필수
    @Override
    public void onAccuracyChanged(android.hardware.Sensor sensor, int i) {
    }

    




}