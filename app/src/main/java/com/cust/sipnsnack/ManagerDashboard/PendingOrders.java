package com.cust.sipnsnack.ManagerDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingOrders extends AppCompatActivity {

    Button goBackBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    LoadingDialog loadingDialog;
    ArrayList<MyOrders> myOrder;
    MyOrdersAdapter ordersAdapter;
    int loopSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        loadingDialog = new LoadingDialog(PendingOrders.this);

        goBackBtn = findViewById(R.id.goBackBTN);
        recyclerView = (RecyclerView) findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myOrder = new ArrayList<MyOrders>();

        ReadFromDB();

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManageOrders.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ManageOrders.class));
        finish();
    }

    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            loopSize++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Pending")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;
                        MyOrdersData.customerUsername = new String[loopSize];
                        MyOrdersData.customerName = new String[loopSize];
                        MyOrdersData.customerPhoneNo = new String[loopSize];
                        MyOrdersData.customerAddress = new String[loopSize];
                        MyOrdersData.paymentType = new String[loopSize];
                        MyOrdersData.receiptImage = new String[loopSize];
                        MyOrdersData.orderDate = new String[loopSize];
                        MyOrdersData.orderTime = new String[loopSize];
                        MyOrdersData.orderId = new String[loopSize];

                        loadingDialog.dismissDialog();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            MyOrdersData.orderId[j] = snapshot.getKey().toString();
                            MyOrdersData.customerUsername[j] = snapshot.child("Username").getValue().toString();
                            MyOrdersData.customerName[j] = snapshot.child("Name").getValue().toString();
                            MyOrdersData.customerPhoneNo[j] = snapshot.child("PhoneNo").getValue().toString();
                            MyOrdersData.customerAddress[j] = snapshot.child("Address").getValue().toString();
                            MyOrdersData.paymentType[j] = snapshot.child("PaymentType").getValue().toString();
                            MyOrdersData.receiptImage[j] = snapshot.child("ReceiptImage").getValue().toString();
                            MyOrdersData.orderDate[j] = snapshot.child("OrderDate").getValue().toString();
                            MyOrdersData.orderTime[j] = snapshot.child("OrderTime").getValue().toString();

                            j++;
                        }

                        for (int i = 0; i < MyOrdersData.customerUsername.length; i++) {
                            myOrder.add(new MyOrders(
                                    MyOrdersData.customerUsername[i],
                                    MyOrdersData.customerName[i],
                                    MyOrdersData.customerPhoneNo[i],
                                    MyOrdersData.customerAddress[i],
                                    MyOrdersData.paymentType[i],
                                    MyOrdersData.receiptImage[i],
                                    MyOrdersData.orderDate[i],
                                    MyOrdersData.orderTime[i],
                                    MyOrdersData.orderId[i]
                            ));
                        }

                        if (loopSize == 0) {
                            Toast.makeText(PendingOrders.this, "No Order Founded ...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), ManageOrders.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ordersAdapter = new MyOrdersAdapter(myOrder);
                            recyclerView.setAdapter(ordersAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}