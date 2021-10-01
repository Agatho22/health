package com.aman.health;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        ArrayList<Exercise> items;
        Context context;

        public RecyclerViewAdapter(ArrayList<Exercise> items, Context context) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, parent, false);
            RecyclerViewAdapter.MyViewHolder viewHolder = new RecyclerViewAdapter.MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Exercise exercise = items.get(position);

            holder.tv.setText(items.get(position).getName());
            holder.tv2.setText(items.get(position).getText());
            holder.iv.setImageResource(items.get(position).getImage());

            holder.btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ExecDetailActivity.class);

                    intent.putExtra("getName", items.get(position).getName());
                    intent.putExtra("getImage", items.get(position).getImage());
                    intent.putExtra("getText", items.get(position).getText());


                    context.startActivity(intent);
                }
            });

            Log.e("Viewpager", "onBindViewHolder" + exercise);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView iv;
            public TextView tv, tv2;
            public Button btn1;

            public MyViewHolder(View itemView) {
                super(itemView);

                tv =  itemView.findViewById(R.id.tv);
                iv =  itemView.findViewById(R.id.iv);
                tv2 =  itemView.findViewById(R.id.tv2);
                btn1 = itemView.findViewById(R.id.btn1);
            }

        }
    }
}