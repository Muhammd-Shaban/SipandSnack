package com.cust.sipnsnack.Customers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.sipnsnack.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentProcess extends AppCompatActivity {

    RadioButton cod, online;
    Button nextBtn;
    private DatabaseReference mDatabaseRef;
    LoadingDialog loadingDialog;
    SharedPreferences spr;
    String username, url;
    ImageView BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_process);

        loadingDialog = new LoadingDialog(PaymentProcess.this);

        spr = getSharedPreferences("LoginSPR", MODE_PRIVATE);
        username = spr.getString("Username", "");

        BackBtn = findViewById(R.id.backBTN);

        url = "none";

        cod = findViewById(R.id.cashOnDelivery);
        online = findViewById(R.id.onlinePayment);
        nextBtn = findViewById(R.id.nextBtn);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TotalBill.class));
                finish();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cod.isChecked()) {
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("temp_order")
                            .child(username);
                    mDatabaseRef.child("PaymentType").setValue("Cash on Delivery");
                    mDatabaseRef.child("ReceiptImage").setValue(url);
                    String qtyy = getIntent().getStringExtra("Qty");
                    String bill = getIntent().getStringExtra("Bill");
                    Intent it = new Intent(getApplicationContext(), SetDeliveryAddress.class);
                    it.putExtra("Qty", qtyy);
                    it.putExtra("Bill", bill);
                    startActivity(it);
                } else {
                    String qtyy = getIntent().getStringExtra("Qty");
                    String bill = getIntent().getStringExtra("Bill");
                    Intent it = new Intent(getApplicationContext(), OnlinePayment.class);
                    it.putExtra("Qty", qtyy);
                    it.putExtra("Bill", bill);
                    startActivity(it);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(getApplicationContext(), TotalBill.class);
        startActivity(it);
        finish();
    }
}