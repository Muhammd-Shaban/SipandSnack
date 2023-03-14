package com.cust.sipnsnack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sipnsnack.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button btn;
    EditText et;
    DatabaseReference reff;
    TabLayout tabLayout;
    ViewPager viewPager;
    float v=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.mainPageTabLayout);
        viewPager = findViewById(R.id.mainPageViewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        /*final LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), getApplicationContext(), tabLayout.getTabCount());
        viewPager.setAdapter(loginAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTranslationY(0);
        tabLayout.setAlpha(0);*/


    }
}