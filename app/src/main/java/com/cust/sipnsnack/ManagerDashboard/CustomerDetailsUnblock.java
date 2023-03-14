package com.cust.sipnsnack.ManagerDashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerDetailsUnblock extends AppCompatActivity {

    TextView customerUsername, customerName, customerOrders, customerDT, customerStatus;
    Button okBtn;
    ImageView unblock;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = mDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details_unblock);

        customerUsername = findViewById(R.id.usernameRight);
        customerName = findViewById(R.id.nameRight);
        customerOrders = findViewById(R.id.cancelledOrdersRight);
        customerStatus = findViewById(R.id.statusRight);
        customerDT = findViewById(R.id.customersDetailTV);

        okBtn = findViewById(R.id.okCustomersButton);

        unblock = findViewById(R.id.customersUnBlockIV);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageCustomers.class);
                startActivity(intent);
                finish();
            }
        });

        customerDT.setText(MyCustomersAdapter.username);
        customerUsername.setText(MyCustomersAdapter.username);
        customerName.setText(MyCustomersAdapter.name);
        customerOrders.setText(MyCustomersAdapter.cancelOrder);
        customerStatus.setText(MyCustomersAdapter.status);

        unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unBlockCustomerDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ManageCustomers.class);
        startActivity(intent);
        finish();
    }

    public void unBlockCustomerDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.ask_unblock_customer_dialog, null);
        Button yesBTN = view.findViewById(R.id.yesBTN);
        Button noBTN = view.findViewById(R.id.noBTN);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();
        yesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uN = customerUsername.getText().toString();
                mDatabaseRef = mDatabase.getReference().child("Users").child("Customers").child(uN);
                mDatabaseRef.child("Status").setValue("Unblocked");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Context context = v.getContext();
                Intent intent = new Intent(context, ManageCustomers.class);
                context.startActivity(intent);
                Toast.makeText(context, "Customer Un-Blocked Successfully...", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        noBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}