package com.aman.health;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Exercise> items;
    Context context;

    public RecyclerViewAdapter(ArrayList<Exercise> items, Context context) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Exercise exercise = items.get(position);

        holder.tv.setText(items.get(position).getName());
        holder.tv2.setText(items.get(position).getText());
        holder.iv.setImageResource(items.get(position).getImage());

        holder.iv.setOnClickListener(new View.OnClickListener() {
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        public TextView tv, tv2;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.tv);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
        }

    }
}
