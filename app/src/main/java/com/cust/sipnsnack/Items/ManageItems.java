package com.cust.sipnsnack.Items;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import com.cust.sipnsnack.ManagerDashboard.DashBoard;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sipnsnack.R;

public class ManageItems extends AppCompatActivity {

    Button addItems, viewItems, browseItems;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_items);

        addItems = findViewById(R.id.addItemsBtn);
        viewItems = findViewById(R.id.viewItemsBtn);
        browseItems = findViewById(R.id.browseItemsBtn);
        backBtn = findViewById(R.id.backBTN);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                finish();
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), AddItems.class);
                startActivity(it);
            }
        });

        viewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), ViewItems.class);
                startActivity(it);
            }
        });

        browseItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), BrowseItems.class);
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