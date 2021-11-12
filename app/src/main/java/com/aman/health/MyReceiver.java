package com.aman.health;

import static com.aman.health.FbData.Alarm;
import static com.aman.health.FbData.mDatabaseRefU;
import static com.aman.health.FbData.wtdayaction;
import static com.aman.health.FbData.wtweekaction;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class MyReceiver extends BroadcastReceiver {

    private Context context;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        SharedPreferences pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        int waterintake = pref.getInt("watercount", 0);


        // 다음 스니펫은 사용자가 알림을 탭하면 활동을 여는 기본 인텐트를 만드는 방법을 보여줍니다.
        Intent Notiintent = new Intent(context, HomeActivity.class);
        Notiintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, Notiintent, 0); //PendingIntent->앱이 꺼져 있오도 원격으로 킬 수가 있는 거


        // 알림의 콘텐츠와 채널 설정
        NotificationCompat.Builder builderWtDay = new NotificationCompat.Builder(context, "water_intake_channel_id")
                .setSmallIcon(R.drawable.workout)  // 작은 아이콘
                .setContentTitle("하루동안 물을 마시지 않았습니다")  // 제목
                .setContentText("물을 마시고 섭취량을 기록해보세요")  // 본문 텍스트
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // 알림 우선순위
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                //밑에는 intent 사용해서 작성한거
                .setContentIntent(pendingIntent) // 사용자가 탭하면 자동으로 알림을 삭제
                .setAutoCancel(true);
        NotificationCompat.Builder builderWtWeek = new NotificationCompat.Builder(context, "water_intake_channel_id")
                .setSmallIcon(R.drawable.workout)
                .setContentTitle("물을 자주 마셔야 합니다")
                .setContentText("지난 일주일간 물 섭취량이 기준 미달이에요")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Log.d(String.valueOf(this), "" + intent.getAction());
        switch (intent.getAction()) {
            case Alarm:
                //Log.d(String.valueOf(this), "물, 스텝 업로드");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, MyService.class));
                } else {
                    context.startService(new Intent(context, MyService.class));
                }
                break;
            case wtdayaction:

                if (waterintake == 0) {
                    Log.d(String.valueOf(this), "물섭취 데일 조건문 통과");
                    notificationManager.notify(0, builderWtDay.build()); // 0 줌
                }
                break;
            case wtweekaction:
                ArrayList<Integer> num = new ArrayList<>();
                mDatabaseRefU.child("watercount").limitToFirst(7).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Log.d(String.valueOf(this), "" + snapshot.getValue(int.class));
                            num.add(snapshot.getValue(int.class));
                        }
                        int avg = num.stream().mapToInt(Integer::intValue).sum() / num.size();
                        Log.d(String.valueOf(this), ""+ avg);
                        if (avg <= 2100) {
                            Log.d(String.valueOf(this), "물 섭취 week 노티");
                            notificationManager.notify(1, builderWtWeek.build()); // 0 줌
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("Database", "Failed to read value.", error.toException());
                    }
                });
                break;
            default:
                Log.d(String.valueOf(this), "정의되지 않은 Action");
        }
    }
}