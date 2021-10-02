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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private ArrayList<Exercise> items;
    private Exercise save;

    public SearchFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        items = new ArrayList<>();
        values();


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items, context);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void values() {

        save = new Exercise();

        save.setName("pushup");
        save.setText("fighting1");
        save.setImage(R.drawable.pushup);

        items.add(save);

        save = new Exercise();

        save.setName("lunge");
        save.setText("fighting2");
        save.setImage(R.drawable.lunge);

        items.add(save);
    }

}