package com.aman.health;

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
import android.widget.TextView;

public class MainFragment extends Fragment {

    Button btn_gowater, btn_logout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btn_gowater = (Button) view.findViewById(R.id.btn_gowater);
        btn_logout = (Button) view.findViewById(R.id.btn_logout);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
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


        btn_gowater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), WaterActivity.class));
            }
        });

        return view;
    }
}