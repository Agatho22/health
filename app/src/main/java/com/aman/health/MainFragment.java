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

import com.dinuscxj.progressbar.CircleProgressBar;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Objects;

public class MainFragment extends Fragment {

    Button btn_logout;

    int water;
    EditText et_water;
    Button btn_select, btn_clear, btn_music, btn_ml3, btn_ml4, btn_ml5, btn_ml6;
    TextView wa_info, wa_info22, watertext;
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
        btn_select = view.findViewById(R.id.btn_select);
        btn_clear = view.findViewById(R.id.btn_clear);
        et_water = view.findViewById(R.id.et_water);

        btn_ml3 = view.findViewById(R.id.btn_ml3);
        btn_ml4 = view.findViewById(R.id.btn_ml4);
        btn_ml5 = view.findViewById(R.id.btn_ml5);
        btn_ml6 = view.findViewById(R.id.btn_ml6);
        watertext = view.findViewById(R.id.watertext);

        btn_ml3.setOnClickListener(this::onClickButton);
        btn_ml4.setOnClickListener(this::onClickButton);
        btn_ml5.setOnClickListener(this::onClickButton);
        btn_ml6.setOnClickListener(this::onClickButton);


        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        water = pref.getInt("watercount", 0);
        wa_info22.setText("오늘 하루목표 물 섭취까지 남은량 :" + (2000 - water) + "ml");
        wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");

        circleProgressBar = view.findViewById(R.id.days_graph);
        circleProgressBar.setMax(2000);


        btn_select.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View view) {
                int i_water = Integer.parseInt(et_water.getText().toString());
                if (i_water < 0) {
                    Toast.makeText(getActivity(), "잘못입력하셨습니다. ", Toast.LENGTH_SHORT).show();
                } else if ((water + i_water) > 2000) {
                    Toast.makeText(getActivity(), i_water + "ml추가완료. ", Toast.LENGTH_SHORT).show();
                    water += i_water;
                    wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");
                    wa_info22.setText("오늘 하루 물 목표 섭취까지 남은량 :" + 0 + "ml");
                } else {
                    Toast.makeText(getActivity(), i_water + "ml추가완료. ", Toast.LENGTH_SHORT).show();
                    water += i_water;
                    wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");
                    wa_info22.setText("오늘 하루 물 목표 섭취까지 남은량 :" + (2000 - water) + "ml");
                }
            }
        });

        onClickButton(view);


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                water = 0;
                circleProgressBar.setProgress(0);
            }
        });


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

    public void onClickButton(View v) {
        progressAnimator = ObjectAnimator.ofInt(circleProgressBar, "progress", circleProgressBar.getProgress(), water);
        switch (v.getId()) {
            case R.id.btn_ml3:
                water += 180;
                wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");
                progressAnimator.setIntValues(water);
                progressAnimator.setDuration(300).start();
                break;
            case R.id.btn_ml4:
                water += 200;
                wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");
                progressAnimator.setIntValues(water);
                progressAnimator.setDuration(400).start();
                break;
            case R.id.btn_ml5:
                water += 250;
                wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");
                progressAnimator.setIntValues(water);
                progressAnimator.setDuration(400).start();
                break;
            case R.id.btn_ml6:
                water += 300;
                wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");
                progressAnimator.setIntValues(water);
                progressAnimator.setDuration(400).start();
                break;
        }

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