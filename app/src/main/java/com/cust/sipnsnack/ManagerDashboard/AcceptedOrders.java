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

public class AcceptedOrders extends AppCompatActivity {

    Button goBackBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    LoadingDialog loadingDialog;
    ArrayList<MyOrders> myOrder;
    AcceptedOrdersAdapter ordersAdapter;
    int loopSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_orders);

        loadingDialog = new LoadingDialog(AcceptedOrders.this);

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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Accepted")
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

        FirebaseDatabase.getInstance().getReference().child("Orders").child("Accepted")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int j = 0;

                        AcceptedOrdersData.orderId = new String[loopSize];
                        AcceptedOrdersData.customerUsername = new String[loopSize];
                        AcceptedOrdersData.customerName = new String[loopSize];
                        AcceptedOrdersData.customerPhoneNo = new String[loopSize];
                        AcceptedOrdersData.customerAddress = new String[loopSize];
                        AcceptedOrdersData.paymentType = new String[loopSize];
                        AcceptedOrdersData.receiptImage = new String[loopSize];
                        AcceptedOrdersData.quantity = new String[loopSize];
                        AcceptedOrdersData.bill = new String[loopSize];
                        AcceptedOrdersData.acceptedBy = new String[loopSize];
                        AcceptedOrdersData.orderDate = new String[loopSize];
                        AcceptedOrdersData.orderTime = new String[loopSize];
                        AcceptedOrdersData.addressType = new String[loopSize];
                        AcceptedOrdersData.longitude = new String[loopSize];
                        AcceptedOrdersData.latitude = new String[loopSize];

                        loadingDialog.dismissDialog();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            AcceptedOrdersData.orderId[j] = snapshot.getKey().toString();
                            AcceptedOrdersData.customerUsername[j] = snapshot.child("Username").getValue().toString();
                            AcceptedOrdersData.customerName[j] = snapshot.child("Name").getValue().toString();
                            AcceptedOrdersData.customerPhoneNo[j] = snapshot.child("PhoneNo").getValue().toString();
                            AcceptedOrdersData.addressType[j] = snapshot.child("AddressType").getValue().toString();

                            if (AcceptedOrdersData.addressType[j].equals("Manual")) {
                                AcceptedOrdersData.customerAddress[j] = snapshot.child("Address").getValue().toString();
                                AcceptedOrdersData.latitude[j] = "";
                                AcceptedOrdersData.longitude[j] = "";
                            } else {
                                AcceptedOrdersData.customerAddress[j] = "";
                                AcceptedOrdersData.latitude[j] = snapshot.child("Latitude").getValue().toString();
                                AcceptedOrdersData.longitude[j] = snapshot.child("Longitude").getValue().toString();
                            }

                            AcceptedOrdersData.paymentType[j] = snapshot.child("PaymentType").getValue().toString();
                            AcceptedOrdersData.receiptImage[j] = snapshot.child("ReceiptImage").getValue().toString();
                            AcceptedOrdersData.quantity[j] = snapshot.child("TotalQuantity").getValue().toString();
                            AcceptedOrdersData.bill[j] = snapshot.child("TotalBill").getValue().toString();
                            AcceptedOrdersData.acceptedBy[j] = snapshot.child("AcceptedBy").getValue().toString();
                            AcceptedOrdersData.orderDate[j] = snapshot.child("OrderDate").getValue().toString();
                            AcceptedOrdersData.orderTime[j] = snapshot.child("OrderTime").getValue().toString();

                            j++;
                        }

                        for (int i = 0; i < AcceptedOrdersData.customerUsername.length; i++) {
                            myOrder.add(new MyOrders(
                                    AcceptedOrdersData.customerUsername[i],
                                    AcceptedOrdersData.customerName[i],
                                    AcceptedOrdersData.customerPhoneNo[i],
                                    AcceptedOrdersData.customerAddress[i],
                                    AcceptedOrdersData.paymentType[i],
                                    AcceptedOrdersData.receiptImage[i],
                                    AcceptedOrdersData.quantity[i],
                                    AcceptedOrdersData.bill[i],
                                    AcceptedOrdersData.acceptedBy[i],
                                    AcceptedOrdersData.orderDate[i],
                                    AcceptedOrdersData.orderTime[i],
                                    AcceptedOrdersData.orderId[i],
                                    AcceptedOrdersData.addressType[i],
                                    AcceptedOrdersData.latitude[i],
                                    AcceptedOrdersData.longitude[i]

                            ));
                        }

                        if (loopSize == 0) {
                            Toast.makeText(AcceptedOrders.this, "No Order Founded ...", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), ManageOrders.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ordersAdapter = new AcceptedOrdersAdapter(myOrder);
                            recyclerView.setAdapter(ordersAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}