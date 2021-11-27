package com.aman.health;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aman.health.music.MusicActivity;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Objects;

public class
MainFragment extends Fragment {

    Button btn_logout;

    int water;
    int twater;
    EditText et_water;
    Button btn_save, btn_clear, btn_music, btn_ml3, btn_ml4, btn_ml5, btn_ml6;
    TextView wa_info, wa_info22;
    CircleProgressBar circleProgressBar;
    ObjectAnimator progressAnimator;

    private static final String DEFAULT_PATTERN = "%d%%";


    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btn_logout = view.findViewById(R.id.btn_logout);
        btn_music = view.findViewById(R.id.btn_music);
        wa_info22 = view.findViewById(R.id.wa_info22);
        wa_info = view.findViewById(R.id.wa_info2);
        btn_save = view.findViewById(R.id.btn_save);
        btn_clear = view.findViewById(R.id.btn_clear);

        btn_ml3 = view.findViewById(R.id.btn_ml3);
        btn_ml4 = view.findViewById(R.id.btn_ml4);
        btn_ml5 = view.findViewById(R.id.btn_ml5);
        btn_ml6 = view.findViewById(R.id.btn_ml6);

        btn_ml3.setOnClickListener(this::onClickButton);
        btn_ml4.setOnClickListener(this::onClickButton);
        btn_ml5.setOnClickListener(this::onClickButton);
        btn_ml6.setOnClickListener(this::onClickButton);
        btn_save.setOnClickListener(this::onClickButton);
        btn_clear.setOnClickListener(this::onClickButton);


        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        water = pref.getInt("watercount", 0);
        wa_info22.setText("남은량 : " + (2000 - water) + "ml");
        wa_info.setText("섭취량 : " + water + "ml");


        circleProgressBar = view.findViewById(R.id.days_graph);
        circleProgressBar.setMax(2000);
        circleProgressBar.setProgress(water);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences 에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPreferences 를 불러옵니다. 메인에서 만든 이름으로
                Intent intent = new Intent(getActivity(), LoginActivityF.class);
                getActivity().startActivity(intent);
                SharedPreferences appData = getActivity().getSharedPreferences("appData", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = appData.edit();
                //editor.clear()는 appData 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.apply();
                getActivity().finish();
            }
        });


        btn_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MusicActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    public void onClickButton(View v) {
        progressAnimator = ObjectAnimator.ofInt(circleProgressBar, "progress", circleProgressBar.getProgress(), water);
        switch (v.getId()) {
            case R.id.btn_save:
                water += twater;
                twater = 0;
                circleProgressBar.setProgress(water);
                break;
            case R.id.btn_clear:
                twater = 0;
                circleProgressBar.setProgress(water);
                break;
            case R.id.btn_ml3:
                twater += 180;
                break;
            case R.id.btn_ml4:
                twater += 200;
                break;
            case R.id.btn_ml5:
                twater += 250;
                break;
            case R.id.btn_ml6:
                twater += 300;
                break;
        }
        progressAnimator.setIntValues(water + twater);
        progressAnimator.setDuration(300).start();
        wa_info22.setText("남은량 : " + (2000 - water) + "ml");
        if (water >= 2000) {
            wa_info.setText("목표량 달성!");
            wa_info22.setText("총 섭취량 : " + (water) + "ml");
        } else if (water < 2000)
            wa_info.setText("섭취량 : " + water + "ml");
    }

    @SuppressLint("DefaultLocale")
    public CharSequence format(int progress, int max) {
        return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
    }

    public static class MyValueformatter extends ValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf(value).substring(0, 2) + "/" + String.valueOf(value).substring(2, 4);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        editor.putInt("watercount", water);
        editor.commit(); // 저장
    }

    @Override
    public void onStop() {
        super.onStop();
        editor.putInt("watercount", water);
        editor.commit(); // 저장
    }
}