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

public class DeliveredOrders extends AppCompatActivity {

    Button goBackBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    LoadingDialog loadingDialog;
    ArrayList<MyDeliveredOrders> myOrder;
    int loopSize;
    DeliveredOrdersAdapter ordersAdapter;
    String dbKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_orders);

        loadingDialog = new LoadingDialog(DeliveredOrders.this);

        goBackBtn = findViewById(R.id.goBackBTN);
        recyclerView = (RecyclerView) findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myOrder = new ArrayList<MyDeliveredOrders>();

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManageOrders.class));
                finish();
            }
        });

        ReadFromDB();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ManageOrders.class));
        finish();
    }

    void ReadFromDB() {
        loadingDialog.startLoadingDialog();
        loopSize = 0;

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Delivered")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    private DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        this.dataSnapshot = dataSnapshot;

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                loopSize++;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Delivered")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            int j = 0;
                            DeliveredOrdersData.customerUsername = new String[loopSize];
                            DeliveredOrdersData.customerName = new String[loopSize];
                            DeliveredOrdersData.customerPhoneNo = new String[loopSize];
                            DeliveredOrdersData.customerAddress = new String[loopSize];
                            DeliveredOrdersData.paymentType = new String[loopSize];
                            DeliveredOrdersData.quantity = new String[loopSize];
                            DeliveredOrdersData.bill = new String[loopSize];
                            DeliveredOrdersData.acceptedBy = new String[loopSize];
                            DeliveredOrdersData.bikerUsername = new String[loopSize];
                            DeliveredOrdersData.bikerName = new String[loopSize];
                            DeliveredOrdersData.bikerPhoneNo = new String[loopSize];
                            DeliveredOrdersData.orderDate = new String[loopSize];
                            DeliveredOrdersData.orderTime = new String[loopSize];
                            DeliveredOrdersData.receiptImage = new String[loopSize];
                            DeliveredOrdersData.dbChild = new String[loopSize];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                dbKey = snapshot.getKey();

                                DeliveredOrdersData.customerUsername[j] = snapshot.child("CustomerUsername").getValue().toString();
                                DeliveredOrdersData.customerName[j] = snapshot.child("CustomerName").getValue().toString();
                                DeliveredOrdersData.customerPhoneNo[j] = snapshot.child("CustomerPhoneNo").getValue().toString();
                                DeliveredOrdersData.customerAddress[j] = snapshot.child("CustomerAddress").getValue().toString();
                                DeliveredOrdersData.paymentType[j] = snapshot.child("CustomerPaymentType").getValue().toString();
                                DeliveredOrdersData.quantity[j] = snapshot.child("CustomerTotalQuantity").getValue().toString();
                                DeliveredOrdersData.bill[j] = snapshot.child("CustomerTotalBill").getValue().toString();
                                DeliveredOrdersData.acceptedBy[j] = snapshot.child("AcceptedBy").getValue().toString();
                                DeliveredOrdersData.bikerUsername[j] = snapshot.child("BikerUsername").getValue().toString();
                                DeliveredOrdersData.bikerName[j] = snapshot.child("BikerName").getValue().toString();
                                DeliveredOrdersData.bikerPhoneNo[j] = snapshot.child("BikerPhoneNo").getValue().toString();
                                DeliveredOrdersData.orderDate[j] = snapshot.child("OrderDate").getValue().toString();
                                DeliveredOrdersData.orderTime[j] = snapshot.child("OrderTime").getValue().toString();
                                DeliveredOrdersData.receiptImage[j] = snapshot.child("ReceiptImage").getValue().toString();
                                DeliveredOrdersData.dbChild[j] = dbKey;

                                j++;
                            }

                            for (int i = 0; i < DeliveredOrdersData.customerUsername.length; i++) {
                                myOrder.add(new MyDeliveredOrders(
                                        DeliveredOrdersData.customerUsername[i],
                                        DeliveredOrdersData.customerName[i],
                                        DeliveredOrdersData.customerPhoneNo[i],
                                        DeliveredOrdersData.customerAddress[i],
                                        DeliveredOrdersData.paymentType[i],
                                        DeliveredOrdersData.quantity[i],
                                        DeliveredOrdersData.bill[i],
                                        DeliveredOrdersData.acceptedBy[i],
                                        DeliveredOrdersData.bikerUsername[i],
                                        DeliveredOrdersData.bikerName[i],
                                        DeliveredOrdersData.bikerPhoneNo[i],
                                        DeliveredOrdersData.orderDate[i],
                                        DeliveredOrdersData.orderTime[i],
                                        DeliveredOrdersData.receiptImage[i],
                                        DeliveredOrdersData.dbChild[i], ""

                                ));
                            }

                            if (loopSize == 0) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(DeliveredOrders.this, "No Order Founded ...", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ManageOrders.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ordersAdapter = new DeliveredOrdersAdapter(myOrder);
                                recyclerView.setAdapter(ordersAdapter);
                            }

                        } else {
                            loadingDialog.dismissDialog();
                            Toast.makeText(DeliveredOrders.this, "No Order Founded ...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), ManageOrders.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}