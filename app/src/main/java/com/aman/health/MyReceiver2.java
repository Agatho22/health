package com.aman.health;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Objects;

public class MyReceiver2 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
        int waterintake = pref.getInt("watercount", 0);

        // 다음 스니펫은 사용자가 알림을 탭하면 활동을 여는 기본 인텐트를 만드는 방법을 보여줍니다.
        Intent Notiintent = new Intent(context, MainActivity.class);
        Notiintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, Notiintent, 0); //PendingIntent->앱이 꺼져 있오도 원격으로 킬 수가 있는 거


        // 알림의 콘텐츠와 채널 설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Channel_Id")
                .setSmallIcon(R.drawable.cmyk)  // 작은 아이콘
                .setContentTitle("하루동안 물을 마시지 않았습니다")  // 제목
                .setContentText("물을 마시고 섭취량을 기록해보세요")  // 본문 텍스트
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // 알림 우선순위
                //밑에는 intent 사용해서 작성한거
                .setContentIntent(pendingIntent) // 사용자가 탭하면 자동으로 알림을 삭제
                .setAutoCancel(true);


        if (waterintake == 0) {
            Log.d("MyReceiver2", "물섭취 조건문 통과");
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0, builder.build()); // 0 줌

        }
    }

}