package com.cust.sipnsnack.ManagerDashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sipnsnack.R;

public class ManageOrders extends AppCompatActivity {

    Button pending, accepted, delivered, onTheWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        pending = findViewById(R.id.pendingBtn);
        accepted = findViewById(R.id.acceptedBtn);
        delivered = findViewById(R.id.deliveredBtn);
        onTheWay = findViewById(R.id.onTheWayBtn);

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PendingOrders.class));
                finish();
            }
        });

        accepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AcceptedOrders.class));
                finish();
            }
        });

        onTheWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OnTheWayOrders.class));
                finish();
            }
        });

        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DeliveredOrders.class));
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