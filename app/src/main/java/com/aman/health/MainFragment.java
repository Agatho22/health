package com.aman.health;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainFragment extends Fragment {

    Button btn_logout;

    int water;
    EditText et_water;
    Button btn_select, btn_clear,btn_music;
    TextView wa_info, wa_info22;

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

        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        water = pref.getInt("watercount", 0);
        wa_info22.setText("오늘 하루목표 물 섭취까지 남은량 :" + (2000 - water) + "ml");
        wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");


        btn_select.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View view) {
                int i_water = Integer.parseInt(et_water.getText().toString());
                if (i_water < 0) {
                    Toast.makeText(getActivity(), "잘못입력하셨습니다. ", Toast.LENGTH_SHORT).show();
                } else if ((water + i_water) > 2000) {
                    Toast.makeText(getActivity(), i_water + "ml추가완료. ", Toast.LENGTH_SHORT).show();
                    water += i_water;
                    editor.putInt("watercount", water);
                    editor.commit(); // 저장
                    wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");
                    wa_info22.setText("오늘 하루 물 목표 섭취까지 남은량 :" + 0 + "ml");
                } else {
                    Toast.makeText(getActivity(), i_water + "ml추가완료. ", Toast.LENGTH_SHORT).show();
                    water += i_water;
                    editor.putInt("watercount", water);
                    editor.commit(); // 저장
                    wa_info.setText("오늘 하루 물 섭취량 :" + water + "ml");
                    wa_info22.setText("오늘 하루 물 목표 섭취까지 남은량 :" + (2000 - water) + "ml");
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
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

    @Override
    public void onStop() {
        super.onStop();
        editor.putInt("watercount", water);
        editor.commit(); // 저장
    }
}