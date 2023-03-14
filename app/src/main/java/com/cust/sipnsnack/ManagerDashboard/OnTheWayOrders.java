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

public class OnTheWayOrders extends AppCompatActivity {

    Button goBackBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    LoadingDialog loadingDialog;
    ArrayList<MyOnTheWayOrders> myOrder;
    int loopSize;
    OnTheWayOrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_the_way_orders);

        loadingDialog = new LoadingDialog(OnTheWayOrders.this);

        goBackBtn = findViewById(R.id.goBackBTN);
        recyclerView = (RecyclerView) findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        myOrder = new ArrayList<MyOnTheWayOrders>();

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("On the Way")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            int j = 0;
                            OntheWayOrdersData.customerUsername = new String[loopSize];
                            OntheWayOrdersData.customerName = new String[loopSize];
                            OntheWayOrdersData.customerPhoneNo = new String[loopSize];
                            OntheWayOrdersData.customerAddress = new String[loopSize];
                            OntheWayOrdersData.paymentType = new String[loopSize];
                            OntheWayOrdersData.quantity = new String[loopSize];
                            OntheWayOrdersData.bill = new String[loopSize];
                            OntheWayOrdersData.acceptedBy = new String[loopSize];
                            OntheWayOrdersData.bikerUsername = new String[loopSize];
                            OntheWayOrdersData.bikerName = new String[loopSize];
                            OntheWayOrdersData.bikerPhoneNo = new String[loopSize];
                            OntheWayOrdersData.orderDate = new String[loopSize];
                            OntheWayOrdersData.orderTime = new String[loopSize];

                            loadingDialog.dismissDialog();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                OntheWayOrdersData.orderId[j] = snapshot.getKey().toString();
                                OntheWayOrdersData.customerUsername[j] = snapshot.child("CustomerUsername").getValue().toString();
                                OntheWayOrdersData.customerName[j] = snapshot.child("CustomerName").getValue().toString();
                                OntheWayOrdersData.customerPhoneNo[j] = snapshot.child("CustomerPhoneNo").getValue().toString();
                                OntheWayOrdersData.customerAddress[j] = snapshot.child("CustomerAddress").getValue().toString();
                                OntheWayOrdersData.paymentType[j] = snapshot.child("CustomerPaymentType").getValue().toString();
                                OntheWayOrdersData.quantity[j] = snapshot.child("CustomerTotalQuantity").getValue().toString();
                                OntheWayOrdersData.bill[j] = snapshot.child("CustomerTotalBill").getValue().toString();
                                OntheWayOrdersData.acceptedBy[j] = snapshot.child("AcceptedBy").getValue().toString();
                                OntheWayOrdersData.bikerUsername[j] = snapshot.child("BikerUsername").getValue().toString();
                                OntheWayOrdersData.bikerName[j] = snapshot.child("BikerName").getValue().toString();
                                OntheWayOrdersData.bikerPhoneNo[j] = snapshot.child("BikerPhoneNo").getValue().toString();
                                OntheWayOrdersData.orderDate[j] = snapshot.child("OrderDate").getValue().toString();
                                OntheWayOrdersData.orderTime[j] = snapshot.child("OrderTime").getValue().toString();

                                j++;
                            }

                            for (int i = 0; i < OntheWayOrdersData.orderId.length; i++) {
                                myOrder.add(new MyOnTheWayOrders(
                                        OntheWayOrdersData.customerUsername[i],
                                        OntheWayOrdersData.customerName[i],
                                        OntheWayOrdersData.customerPhoneNo[i],
                                        OntheWayOrdersData.customerAddress[i],
                                        OntheWayOrdersData.paymentType[i],
                                        OntheWayOrdersData.quantity[i],
                                        OntheWayOrdersData.bill[i],
                                        OntheWayOrdersData.acceptedBy[i],
                                        OntheWayOrdersData.bikerUsername[i],
                                        OntheWayOrdersData.bikerName[i],
                                        OntheWayOrdersData.bikerPhoneNo[i],
                                        OntheWayOrdersData.orderDate[i],
                                        OntheWayOrdersData.orderTime[i],
                                        OntheWayOrdersData.orderId[i]

                                ));
                            }

                            if (loopSize == 0) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(OnTheWayOrders.this, "No Order Founded ...", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), ManageOrders.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ordersAdapter = new OnTheWayOrdersAdapter(myOrder);
                                recyclerView.setAdapter(ordersAdapter);
                            }

                        } else {
                            loadingDialog.dismissDialog();
                            Toast.makeText(OnTheWayOrders.this, "No Order Founded ...", Toast.LENGTH_SHORT).show();

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