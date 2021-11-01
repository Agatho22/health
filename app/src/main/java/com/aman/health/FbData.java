package com.aman.health;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FbData {
    public static void resetAlarm(Context context) {
        AlarmManager resetAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(context, MyReceiver.class);

        PendingIntent resetSender = PendingIntent.getBroadcast(context, 0, resetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 자정 시간
        Calendar resetCal = Calendar.getInstance();
        resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY, 1);
        resetCal.set(Calendar.MINUTE, 06);
        resetCal.set(Calendar.SECOND, 40);

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
        //resetAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis(), 1000, resetSender); //테스트용
        resetAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, resetSender); //실사용

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
        String setResetTime = format.format(new Date(resetCal.getTimeInMillis()));

        Log.d("알람 시간", "ResetHour : " + setResetTime);
        Log.d("반복 시간", "ResetHour : " + AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15);
        // 원래 알람메소드 자리

    }

}

