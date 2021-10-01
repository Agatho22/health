package com.aman.health;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;



public class ExecDetailActivity extends AppCompatActivity {

    ArrayList<Exercise> item;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exec_detail);

        Intent intent = getIntent();
        Exercise save= new Exercise();

        save.setName(intent.getExtras().getString("getName"));
        save.setText(intent.getExtras().getString("getText"));
        save.setImage(intent.getExtras().getInt("getImage"));



        imageView = findViewById(R.id.image2);

        imageView.setImageResource(save.getImage());

        Button backbtn = (Button) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExecDetailActivity.this, SearchFragment.class);
                startActivity(intent);
            }
        });
    }

}