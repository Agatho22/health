package com.aman.health;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aman.health.Exercise;
import com.aman.health.R;
import com.aman.health.RecyclerViewAdapter;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private ArrayList<Exercise> items;

    public SearchFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        items = new ArrayList<>();

        Exercise exercise1 = new Exercise(R.drawable.pushup, "pushup", "fighting1");
        items.add(exercise1);
        Exercise exercise2 = new Exercise(R.drawable.lunge, "lunge", "fighting2");
        items.add(exercise2);


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
