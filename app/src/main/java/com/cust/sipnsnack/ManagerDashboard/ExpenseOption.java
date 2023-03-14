package com.cust.sipnsnack.ManagerDashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sipnsnack.R;

public class ExpenseOption extends AppCompatActivity {

    Button addBtn, viewBtn;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_option);

        addBtn = findViewById(R.id.addExpenseBtn);
        viewBtn = findViewById(R.id.viewExpenseBtn);
        backBtn = findViewById(R.id.backBTN);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddExpense.class));
                finish();
            }
        });


        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewExpense.class));
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