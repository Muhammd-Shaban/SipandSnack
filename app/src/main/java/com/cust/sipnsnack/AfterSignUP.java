package com.cust.sipnsnack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sipnsnack.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AfterSignUP extends AppCompatActivity {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntryList;

    ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sign_up);

        categories = new ArrayList<String>();

        categories.add("Specials");
        categories.add("Fries");
        categories.add("Snacks");
        categories.add("Pizza");
        categories.add("Burgers");
        categories.add("Chilled Drinks");
        categories.add("Sea Foods");
        categories.add("Coffees");

        // ..... YAHAN PE CATEGORIES SORT HO RAHI HAIN .....
        Collections.sort(categories, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });

        for (int i=0; i<categories.size(); i++) {
            System.out.println(i+1 + " = " + categories.get(i));
        }



        // BAR GRAPH

        barChart = findViewById(R.id.barChart);

        getEntries();

        barDataSet = new BarDataSet(barEntryList, "Sip n Snack");

        barData = new BarData(barDataSet);

        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(R.color.black);
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);
    }

    public void getEntries() {
        barEntryList = new ArrayList<>();

        barEntryList.add(new BarEntry(1f, 4));
        barEntryList.add(new BarEntry(3f, 6));
        barEntryList.add(new BarEntry(2f, 2));
        barEntryList.add(new BarEntry(4f, 7));
        barEntryList.add(new BarEntry(5f, 3));
    }
}