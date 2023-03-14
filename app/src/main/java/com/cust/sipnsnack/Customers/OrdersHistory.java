package com.cust.sipnsnack.Customers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.sipnsnack.R;
import com.google.android.material.tabs.TabLayout;

public class OrdersHistory extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    public static String customerUsername;
    SharedPreferences spr;
    ImageView back, refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);

        back = findViewById(R.id.backBTN);
        refresh = findViewById(R.id.refreshBTN);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CustomerView.class));
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getIntent());
            }
        });

        customerUsername = spr.getString("Username", "");

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new fragment_adapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(viewPager);
    }
}