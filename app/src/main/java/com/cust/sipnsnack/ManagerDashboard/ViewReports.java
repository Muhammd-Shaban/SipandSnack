package com.cust.sipnsnack.ManagerDashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sipnsnack.R;

public class ViewReports extends AppCompatActivity {

    Button daily, monthly, yearly;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        daily = findViewById(R.id.dayWiseReportsBtn);
        monthly = findViewById(R.id.monthWiseReportsBtn);
        yearly = findViewById(R.id.yearWiseReportsBtn);
        backBtn = findViewById(R.id.backBTN);

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DayWiseReports.class));
                finish();
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MonthWiseReports.class));
                finish();
            }
        });

        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), YearWiseReports.class));
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        finish();
    }
}