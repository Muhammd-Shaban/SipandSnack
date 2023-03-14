package com.cust.sipnsnack.Customers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Delivered_fragment extends Fragment {

    Activity referenceActivity;
    View parentHolder;
    Button returnBtn;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    ArrayList<MyOrdersModel> myOrder;
    TextView tv;
    ImageView notFound;
    DeliveredOrderAdapter ordersAdapter;
    int loopSize;
    String dbUsername, username, name, phone;
    SharedPreferences spr;

    public Delivered_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        referenceActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.delivered_fragment, container,
                false);

        returnBtn = parentHolder.findViewById(R.id.returnBTN);
        recyclerView = (RecyclerView) parentHolder.findViewById(R.id.orders_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(referenceActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tv = parentHolder.findViewById(R.id.emptyTV);
        notFound = parentHolder.findViewById(R.id.notFoundIV);

        spr = getActivity().getSharedPreferences("LoginSPR", Context.MODE_PRIVATE);
        username = spr.getString("Username", "");

        getCustomerInfo(username);

        myOrder = new ArrayList<MyOrdersModel>();

        ReadFromDB();

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), CustomerView.class));
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
                                dbUsername = snapshot.child("CustomerUsername").getValue().toString();

                                if (dbUsername.equals(OrdersHistory.customerUsername)) {
                                    loopSize++;
                                }
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
                        int j = 0;
                        CustomerOrdersData.customerName = new String[loopSize];
                        CustomerOrdersData.paymentType = new String[loopSize];
                        CustomerOrdersData.orderQty = new String[loopSize];
                        CustomerOrdersData.orderBill = new String[loopSize];
                        CustomerOrdersData.orderTime = new String[loopSize];
                        CustomerOrdersData.orderDate = new String[loopSize];
                        CustomerOrdersData.orderId = new String[loopSize];
                        CustomerOrdersData.bikerName = new String[loopSize];
                        CustomerOrdersData.bikerPhone = new String[loopSize];
                        CustomerOrdersData.bikerUsername = new String[loopSize];


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            if (snapshot.exists()) {
                                dbUsername = snapshot.child("CustomerUsername").getValue().toString();

                                if (dbUsername.equals(OrdersHistory.customerUsername)) {
                                    CustomerOrdersData.customerName[j] = snapshot.child("CustomerName").getValue().toString();
                                    CustomerOrdersData.paymentType[j] = snapshot.child("CustomerPaymentType").getValue().toString();
                                    CustomerOrdersData.orderQty[j] = snapshot.child("CustomerTotalQuantity").getValue().toString();
                                    CustomerOrdersData.orderBill[j] = snapshot.child("CustomerTotalBill").getValue().toString();
                                    CustomerOrdersData.orderTime[j] = snapshot.child("OrderTime").getValue().toString();
                                    CustomerOrdersData.orderDate[j] = snapshot.child("OrderDate").getValue().toString();
                                    CustomerOrdersData.orderId[j] = snapshot.getKey().toString();
                                    CustomerOrdersData.bikerName[j] = snapshot.child("BikerName").getValue().toString();
                                    CustomerOrdersData.bikerPhone[j] = snapshot.child("BikerPhoneNo").getValue().toString();
                                    CustomerOrdersData.bikerUsername[j] = snapshot.child("BikerUsername").getValue().toString();

                                    j++;
                                }
                            }
                        }

                        for (int i = 0; i < CustomerOrdersData.customerName.length; i++) {
                            myOrder.add(new MyOrdersModel(
                                    CustomerOrdersData.customerName[i],
                                    CustomerOrdersData.paymentType[i],
                                    CustomerOrdersData.orderQty[i],
                                    CustomerOrdersData.orderBill[i],
                                    CustomerOrdersData.orderTime[i],
                                    CustomerOrdersData.orderDate[i],
                                    CustomerOrdersData.orderId[i],
                                    CustomerOrdersData.bikerName[i],
                                    CustomerOrdersData.bikerPhone[i],
                                    CustomerOrdersData.bikerUsername[i]

                            ));
                        }

                        if (loopSize == 0) {
                            tv.setVisibility(View.VISIBLE);
                            notFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            returnBtn.setVisibility(View.GONE);
                        } else {
                            tv.setVisibility(View.GONE);
                            notFound.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            returnBtn.setVisibility(View.VISIBLE);

                            ordersAdapter = new DeliveredOrderAdapter(myOrder, name, phone);
                            recyclerView.setAdapter(ordersAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void getCustomerInfo(String usn) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(usn)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    DataSnapshot dataSnapshot;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        this.dataSnapshot = snapshot;

                        name = dataSnapshot.child("Name").getValue().toString();
                        phone = dataSnapshot.child("PhoneNo").getValue().toString();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}