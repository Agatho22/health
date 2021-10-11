package com.aman.health;


import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;


public class ExerciseFragment extends Fragment {

    public ExerciseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        //fragment_exercise의 fragment를 불러옴
        //fragment는 SearchFragment와 연결되어있음
        //SearchFragment에서 listview 불러옴

        return view;
    }


}