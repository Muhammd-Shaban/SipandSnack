package com.cust.sipnsnack.ManagerDashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sipnsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeliveredOrdersDetails extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    Button returnBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<MyDeliveredOrders> myOrder;
    TextView tv;
    ImageView notFound;
    DeliveredOrdersAdapter ordersAdapter;
    int loopSize;
    String dbKey;

    public DeliveredOrdersDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.delivered_orders_details, container,
                false);

        returnBtn = parentHolder.findViewById(R.id.returnBTN);
        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(referenceActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tv = parentHolder.findViewById(R.id.emptyTV);
        notFound = parentHolder.findViewById(R.id.notFoundIV);

        myOrder = new ArrayList<MyDeliveredOrders>();

        ReadFromDB();

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), DashBoard.class));
            }
        });

        return parentHolder;
    }

    void ReadFromDB() {
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
                            DeliveredOrdersData.addressType = new String[loopSize];
                            DeliveredOrdersData.longitude = new String[loopSize];
                            DeliveredOrdersData.latitude = new String[loopSize];

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                dbKey = snapshot.getKey();

                                DeliveredOrdersData.customerUsername[j] = snapshot.child("CustomerUsername").getValue().toString();
                                DeliveredOrdersData.customerName[j] = snapshot.child("CustomerName").getValue().toString();
                                DeliveredOrdersData.customerPhoneNo[j] = snapshot.child("CustomerPhoneNo").getValue().toString();
                                DeliveredOrdersData.addressType[j] = snapshot.child("AddressType").getValue().toString();

                                if (DeliveredOrdersData.addressType[j].equals("Manual")) {
                                    DeliveredOrdersData.customerAddress[j] = snapshot.child("CustomerAddress").getValue().toString();
                                    DeliveredOrdersData.longitude[j] = "";
                                    DeliveredOrdersData.latitude[j] = "";
                                } else {
                                    DeliveredOrdersData.customerAddress[j] = "";
                                    DeliveredOrdersData.longitude[j] = snapshot.child("Longitude").getValue().toString();
                                    DeliveredOrdersData.latitude[j] = snapshot.child("Latitude").getValue().toString();
                                }
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
                                        DeliveredOrdersData.dbChild[i],
                                        DeliveredOrdersData.addressType[i]

                                ));
                            }

                            if (loopSize == 0) {
                                tv.setVisibility(View.VISIBLE);
                                notFound.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                                returnBtn.setVisibility(View.INVISIBLE);
                            } else {
                                tv.setVisibility(View.GONE);
                                notFound.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                returnBtn.setVisibility(View.VISIBLE);

                                ordersAdapter = new DeliveredOrdersAdapter(myOrder);
                                recyclerView.setAdapter(ordersAdapter);
                            }

                        } else {
                            tv.setVisibility(View.VISIBLE);
                            notFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                            returnBtn.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}