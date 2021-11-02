package com.aman.health;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FbData {
    public static void resetAlarm(Context context, Class receiver, int H, int M, int S) {
        AlarmManager resetAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(context, receiver);

        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent resetSender = PendingIntent.getBroadcast(context, 0, resetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 자정 시간
        Calendar resetCal = Calendar.getInstance();
        resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY, H);
        resetCal.set(Calendar.MINUTE, M);
        resetCal.set(Calendar.SECOND, S);

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
        resetAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, resetCal.getTimeInMillis(), 1000, resetSender); //테스트용
        //resetAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, resetSender); //실사용

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
        String setResetTime = format.format(new Date(resetCal.getTimeInMillis()));

        Log.d("알람 시간", "ResetHour : " + setResetTime);
        Log.d("반복 시간", "ResetHour : " + setResetTime + AlarmManager.INTERVAL_DAY);
        // 원래 알람메소드 자리

    }

    public static void StepChartset(List<BarEntry> entry_chart, BarChart barchart) {
        BarDataSet barDataSet = new BarDataSet(entry_chart, "Step");
        //여기에 lineDataSet 설정
        barDataSet.setColor(Color.parseColor("#FA757D"));
        barDataSet.setValueTextSize(10f);


        BarData barData = new BarData();
        barData.setDrawValues(false);
        barData.setBarWidth(0.6f);

        barData.addDataSet(barDataSet);

        XAxis xAxis = barchart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setValueFormatter(new ProFileFragment.MyValueformatter());
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis yAxisLeft = barchart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setTextColor(Color.BLACK); //Y축 텍스트 컬러 설정
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setAxisMaximum(10000f);
        yAxisLeft.setGranularity(5000f);
        yAxisLeft.setDrawAxisLine(false);

        //오른쪽 축 비활성화
        YAxis yAxisRight = barchart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);


        barchart.setData(barData);
        barchart.setDescription(null);
        barchart.setPinchZoom(false);
        barchart.animateY(1000);
        barchart.setTouchEnabled(false);
        barchart.invalidate();
        barchart.setDrawGridBackground(false);
    }

    public static void WaterChartset(List<BarEntry> entry_chart, BarChart barchart) {
        BarDataSet barDataSet = new BarDataSet(entry_chart, "Water intake(ml)");
        //여기에 lineDataSet 설정
        barDataSet.setColor(Color.parseColor("#4359FA"));
        barDataSet.setValueTextSize(10f);


        BarData barData = new BarData();
        barData.setDrawValues(false);
        barData.setBarWidth(0.6f);

        barData.addDataSet(barDataSet);

        XAxis xAxis = barchart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setValueFormatter(new ProFileFragment.MyValueformatter());
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis yAxisLeft = barchart.getAxisLeft(); //Y축의 왼쪽면 설정
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setTextColor(Color.BLACK); //Y축 텍스트 컬러 설정
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setAxisMaximum(2000f);
        yAxisLeft.setGranularity(1000f);
        yAxisLeft.setDrawAxisLine(false);

        //오른쪽 축 비활성화
        YAxis yAxisRight = barchart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);


        barchart.setData(barData);
        barchart.setDescription(null);
        barchart.setPinchZoom(false);
        barchart.animateY(1000);
        barchart.setTouchEnabled(false);
        barchart.invalidate();
        barchart.setDrawGridBackground(false);
    }


}

//class MyValueEventListener implements ValueEventListener {
//    List<BarEntry> entry_chart = new ArrayList<>();
//
//    @Override
//    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//            int key = parseInt(Objects.requireNonNull(snapshot.getKey()).substring(4, 8));
//            entry_chart.add(new BarEntry(key, snapshot.getValue(long.class)));
//            Log.d("차트", "" + key);
//
//        }
//        Log.d("차트", "" + entry_chart);
//
//        BarDataSet barDataSet = new BarDataSet(entry_chart, "Step");
//        //여기에 lineDataSet 설정
//        barDataSet.setColor(Color.parseColor("#3F52E3"));
//        barDataSet.setValueTextSize(10f);
//
//
//        BarData barData = new BarData();
//        barData.setDrawValues(false);
//        barData.setBarWidth(0.6f);
//
//        barData.addDataSet(barDataSet);
//
//        XAxis xAxis = barchart.getXAxis(); // x 축 설정
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
//        xAxis.setValueFormatter(new ProFileFragment.MyValueformatter());
//        xAxis.setDrawLabels(true);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(true);
//
//        YAxis yAxisLeft = barchart.getAxisLeft(); //Y축의 왼쪽면 설정
//        yAxisLeft.setDrawGridLines(false);
//        yAxisLeft.setTextColor(Color.BLACK); //Y축 텍스트 컬러 설정
//        yAxisLeft.setAxisMinimum(0f);
//        yAxisLeft.setAxisMaximum(10000f);
//        yAxisLeft.setGranularity(5000f);
//        yAxisLeft.setDrawAxisLine(false);
//
//        //오른쪽 축 비활성화
//        YAxis yAxisRight = barchart.getAxisRight();
//        yAxisRight.setDrawLabels(false);
//        yAxisRight.setDrawAxisLine(false);
//        yAxisRight.setDrawGridLines(false);
//
//
//        barchart.setData(barData);
//        barchart.setDescription(null);
//        barchart.setPinchZoom(false);
//        barchart.animateY(1000);
//        barchart.setTouchEnabled(false);
//        barchart.invalidate();
//        barchart.setDrawGridBackground(false);
//    }
//
//    @Override
//    public void onCancelled(DatabaseError error) {
//        Log.w("Database", "Failed to read value.", error.toException());
//    }
//}


