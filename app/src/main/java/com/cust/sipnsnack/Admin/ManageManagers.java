package com.cust.sipnsnack.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sipnsnack.R;

public class ManageManagers extends AppCompatActivity {

    Button addManagers, viewManagers;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_managers);

        addManagers = findViewById(R.id.addManagersBtn);
        viewManagers = findViewById(R.id.viewManagersBtn);
        backBtn = findViewById(R.id.backBTN);

        addManagers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), AddManagers.class);
                startActivity(it);
                finish();
            }
        });

        viewManagers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ViewManagers.class);
                startActivity(it);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminView.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), AdminView.class);
        startActivity(it);
        finish();
    }
}