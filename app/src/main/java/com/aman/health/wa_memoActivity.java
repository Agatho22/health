package com.aman.health;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class wa_memoActivity extends AppCompatActivity {

    BarChart stackedChart;
    int[] colorArray = new int[] {Color.BLUE, Color.WHITE, Color.WHITE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wa_memo);
        stackedChart = findViewById(R.id.stacked_barchart);
        stackedChart.setTouchEnabled(false); //그래프 확대 못하게
        BarDataSet barDataSet = new BarDataSet(dataValues1(),"수분 섭취량");
        barDataSet.setColors(colorArray);

        BarData barData = new BarData(barDataSet);
        stackedChart.setData(barData);
    }
    private ArrayList<BarEntry> dataValues1(){
        ArrayList<BarEntry> dataVals = new ArrayList<>();

        dataVals.add(new BarEntry(1,new float[]{1900,0f,4}));
        dataVals.add(new BarEntry(2,new float[]{1200,0f,4}));
        dataVals.add(new BarEntry(3,new float[]{1700,0f,4}));
        dataVals.add(new BarEntry(4,new float[]{2000,0f,4}));
        dataVals.add(new BarEntry(5,new float[]{1200,0f,4}));
        dataVals.add(new BarEntry(6,new float[]{1800,0f,4}));
        dataVals.add(new BarEntry(7,new float[]{1700,0f,4}));
        return dataVals;
    }
}