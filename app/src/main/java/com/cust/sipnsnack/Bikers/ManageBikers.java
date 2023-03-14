package com.cust.sipnsnack.Bikers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import com.cust.sipnsnack.ManagerDashboard.DashBoard;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sipnsnack.R;

public class ManageBikers extends AppCompatActivity {

    Button addBikers, viewBikers;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bikers);

        addBikers = findViewById(R.id.addBikersBtn);
        viewBikers = findViewById(R.id.viewBikersBtn);
        backBtn = findViewById(R.id.backBTN);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                finish();
            }
        });

        addBikers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), AddBikers.class);
                startActivity(it);
            }
        });

        viewBikers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), ViewBikers.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), DashBoard.class);
        startActivity(it);
        finish();
    }
}