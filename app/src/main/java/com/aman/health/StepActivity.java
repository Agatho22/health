package com.aman.health;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class StepActivity extends AppCompatActivity implements View.OnClickListener {

    String [] permission_list = {
            Manifest.permission.ACTIVITY_RECOGNITION
    };

    private StepService stepService; // 서비스 클래스 객체를 선언
    boolean isService = false; // 서비스 중인 확인용

    private TextView textCount, statusService;
    private Button startBtn, endBtn;
    private Intent intent; //서비스 객체를 가지고 있는 인텐트 객체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        stepService = new StepService();
        startBtn = findViewById(R.id.startBtn);
        endBtn = findViewById(R.id.endBtn);
        textCount = findViewById(R.id.textCount);
        statusService = findViewById(R.id.textStatusService);
        setListener();

    }

    private StepCallback stepCallback = new StepCallback() { //서비스 내부로 Set되어 스텝카운트의 변화와 Unbind의 결과를 전달하는 콜백 객체의 구현체
        @Override
        public void onStepCallback(int step) {
            textCount.setText("" + step);
        }

        @Override
        public void onUnbindService() {
            isService = false;
            statusService.setText("해제됨");
            Toast.makeText(StepActivity.this, "디스바인딩", Toast.LENGTH_SHORT).show();
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() { //서비스 바인드를 담당하는 객체의 구현체
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(StepActivity.this, "예스바인딩", Toast.LENGTH_SHORT).show();
            StepService.MyBinder mb = (StepService.MyBinder) service;
            stepService = mb.getService(); //
            stepService.setCallback(stepCallback);
            isService = true;
            statusService.setText("연결됨");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { //요거는 사실상 서비스가 킬되거나 아예 죽임 당했을 때만 호출된다고 보시면 됨

            // stopService 또는 unBindService때 호출되지 않음.
            isService = false;
            statusService.setText("해제됨");
            Toast.makeText(StepActivity.this, "디스바인딩", Toast.LENGTH_SHORT).show();
        }
    };


    public void setListener() {
        startBtn.setOnClickListener(this);
        endBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.startBtn:
                intent = new Intent(this, StepService.class);
                startService(intent);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.endBtn:
                try {
                    stopService(intent);
                    unbindService(serviceConnection);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    @Override
    protected void onStop() {//액티비티를 벗어났을 땐 서비스와 액티비티의 바인드를 끊어줌 : 백그라운드에서 UI를 건들지 않게 하기 위함
        super.onStop();
        unbindService(serviceConnection);
    }

    // 권한 체크 메소드
    public void checkPermission(){
        //현재 안드로이드 버전이 6.0 미만이면 메소드를 종료
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        //각 권한의 허용 여부를 확인
        for(String permission : permission_list){
            //권한 허용 여부를 확인
            int chk = checkCallingOrSelfPermission(permission);
            //거부 상태일시
            if(chk == PackageManager.PERMISSION_DENIED){
                // 사용자에게 권한 허용여부를 확인하는 창을 띄움
                requestPermissions(permission_list, 0);
            }
        }
    }
}

